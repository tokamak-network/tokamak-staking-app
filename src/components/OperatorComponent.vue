<template>
  <view class="operator-wrap">
    <touchable-opacity :on-press="pressed === false ? openOperator : null">
      <view class="operator-container">
        <image
          class="operator-img"
          :source="
            operator.name === 'test'
              ? tokamak
              : operator.name === 'DXM Corp'
              ? dxm
              : dsrv
          "
        >
        </image>
        <view class="operator-text-container">
          <text class="operator-title">{{ operator.name }}</text>
          <touchable-opacity
            :on-press="pressed === true ? openInformation : openOperator"
          >
            <text
              :class="{
                selected: pressed === true,
                'operator-content': pressed !== true,
              }"
              >{{ pressed === false ? info : "More Information" }}
            </text>
          </touchable-opacity>
        </view>
        <touchable-opacity v-if="pressed === true" :on-press="closeOperator">
          <image
            class="operator-img-close"
            :source="pressed === true ? CloseIcon : null"
          >
          </image>
        </touchable-opacity>
      </view>
    </touchable-opacity>
    <view v-if="pressed" class="operator-detail">
      <view class="divider" />
      <view class="operator-detail-container">
        <view class="operator-detail-text">
          <text class="operator-detail-title">Commission Rate</text>
          <text class="operator-detail-content"
            >{{ operator.isCommissionRateNegative === 1 ? "-" : ""
            }}{{ rateOf(operator.commissionRate) }}</text
          >
        </view>
        <view class="operator-detail-text">
          <text class="operator-detail-title">Most recent Commit</text>
          <text class="operator-detail-content">{{
            fromNow(operator.lastFinalizedAt)
          }}</text>
        </view>
        <view class="operator-detail-text">
          <text class="operator-detail-title">My Staked</text>
          <text class="operator-detail-content">{{
            currencyAmount(operator.userStaked)
          }}</text>
        </view>
        <view class="divider" />
        <view class="operator-detail-text">
          <text class="operator-detail-title">Available Amount</text>
          <text class="operator-detail-content">{{
            currencyAmount(tonBalance)
          }}</text>
        </view>
        <view class="operator-detail-input">
          <text class="info-title">Amount</text>
          <text-input
            class="value-input"
            v-model="amountToDelegate"
            placeholder="0.00"
            autocomplete="off"
            minlength="1"
            maxlength="1"
            keyboardType="numeric"
          />
          <text class="info-title">TON</text>
        </view>
        <touchable-opacity :on-press="onPressButton">
          <button-main title="Stake" />
        </touchable-opacity>
      </view>
    </view>
  </view>
</template>
<script>
import ButtonMain from "@/components/ButtonMain";
import tokamak from "../../assets/tokamak.png";
import dxm from "../../assets/dxm.png";
import dsrv from "../../assets/dsrv.png";
import CloseIcon from "../../assets/icon-close.png";
import { mapState, mapGetters } from "vuex";
import { getConfig } from "../../config.js";
import { NativeModules } from "react-native";
const { BlockchainModule } = NativeModules;
const _TON = createCurrency("TON");
const _WTON = createCurrency("WTON");
import { BN, padLeft } from "web3-utils";
import { createCurrency } from "@makerdao/currency";
import { ToastAndroid } from "react-native";


export default {
  data() {
    return {
      tokamak,
      dxm,
      dsrv,
      CloseIcon,
      pressed: false,
      opendNow: null,
      amountToDelegate: "",
    };
  },
  components: {
    "button-main": ButtonMain,
  },
  props: {
    layer2: {
      required: true,
      type: String,
    },
    navigation: {
      type: Object,
    },
  },
  computed: {
    ...mapState([
      "user",
      "DepositManager",
      "tonBalance",
      "blockNumber",
      "user",
      "TON",
      "WTON",
      "SeigManager",
    ]),
    ...mapGetters(["operatorByLayer2"]),
    operator() {
      return this.operatorByLayer2(this.layer2);
    },
    rateOf() {
      return (commissionRate) => this.$options.filters.rateOf(commissionRate);
    },
    fromNow() {
      return (timestamp, suffix = false) =>
        this.$options.filters.fromNow(timestamp, suffix);
    },
    info() {
      const commission = "Commission Rate: ";
      const committed = "Last Committed: ";
      const isNeg = this.operator.isCommissionRateNegative === 1 ? "-" : "";
      const rate = isNeg + this.rateOf(this.operator.commissionRate);
      const lastComm = this.fromNow(this.operator.lastFinalizedAt);
      return commission + rate + " " + committed + lastComm;
    },
    currencyAmount() {
      return (amount) => this.$options.filters.currencyAmount(amount);
    },
  },
  methods: {
    openOperator() {
      this.pressed = true;
      //  this.navigation.push('SelectedOperator', {name: "tokamak"} );
    },
    closeOperator() {
      this.pressed = false;
    },
    openInformation() {
      console.log("openInfo");
    },
    async onPressButton() {
      if (
        this.amountToDelegate === "" ||
        parseFloat(this.amountToDelegate) === 0
      ) {
        return alert("Please check your TON amount.");
        //  ToastAndroid.show("Please check your TON amount.", ToastAndroid.SHORT);
      }
      if (_TON(this.amountToDelegate).gt(this.tonBalance)) {
        return alert("Please check your TON amount.");
      }
      const amount = _TON(this.amountToDelegate).toFixed("wei");
      const data = this.getData();
      const status = await BlockchainModule.approveAndCall(
        this.TON,
        "approveAndCall",
        this.WTON,
        amount,
        data
      );
      if (status.code === 0) {
        this.index = 0;
        ToastAndroid.show("Transaction Successful", ToastAndroid.SHORT);
        const transaction = {
          from: this.user,
          type: "Withdrawn",
          amount: amount,
          transactionHash: status.hash,
          target: this.operator.layer2,
        };
        //  this.$store.dispatch('addPendingTransaction', transaction);
        this.$store.dispatch("setBalance");
        this.amountToDelegate = "";
      } else {
        ToastAndroid.show("Transaction Unsuccessful", ToastAndroid.SHORT);
      }
    },
    increaseIndex() {
      this.index++;
      if (
        this.operator.withdrawalRequests.length === 0 ||
        this.index === this.operator.withdrawalRequests.length
      ) {
        this.index = 0;
      }
    },
    isNumber(evt) {
      evt = evt ? evt : window.event;
      const charCode = evt.which ? evt.which : evt.keyCode;
      if (
        charCode > 31 &&
        (charCode < 48 || charCode > 57) &&
        charCode !== 46
      ) {
        evt.preventDefault();
      } else {
        return true;
      }
    },
    marshalString(str) {
      if (str.slice(0, 2) === "0x") return str;
      return str;
    },
    unmarshalString(str) {
      if (str.slice(0, 2) === "0x") return str.slice(2);
      return str;
    },
    getData() {
      const data = this.marshalString(
        [this.DepositManager, this.operator.layer2]
          .map(this.unmarshalString)
          .map((str) => padLeft(str, 64))
          .join("")
      );
      return data;
    },
  },
};
</script>
<style scoped>
.operator-wrap {
  display: flex;
  flex-direction: column;
  width: 320px;
  padding: 15px 61px 15px 15px;
  border-width: 1;
  border-color: #e7ebf2;
  border-radius: 1px;
  background-color: #ffffff;
  margin-bottom: 15px;
}
.operator-container {
  display: flex;
  flex-direction: row;
}
.operator-img {
  width: 35px;
  height: 35px;
  margin-right: 18px;
}
.operator-img-close {
  width: 24px;
  height: 24px;
  align-self: flex-start;
  margin-left: 118px;
}
.operator-title {
  font-size: 17px;
  font-weight: 900;
  color: #131315;
}
.operator-content {
  font-size: 11px;
  color: #86929d;
}
.selected {
  font-size: 11px;
  color: #2a72e5;
  margin-bottom: 16px;
}
.divider {
  width: 280px;
  height: 1px;
  background-color: #e7ebf2;
  margin-bottom: 15px;
}
.operator-detail {
  display: flex;
  align-self: center;
}
.operator-detail-text {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  height: 35px;
}
.operator-detail-input {
  width: 280px;
  height: 36px;
  border-width: 1px;
  border-radius: 1px;
  border-color: #dfe4ee;
  margin-bottom: 15px;
  padding: 9px, 10px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-self: stretch;
  align-items: center;
  position: relative;
}
.operator-detail-btn {
  width: 280px;
  height: 40px;
  border-radius: 4px;
}
.value-input {
  text-align: right;
  height: 38px;
  font-size: 18px;
  width: 181px;
  overflow: hidden;
  /* width: 120px; */
  /* margin-right: 20px; */
  align-items: center;
  color: #555555;
  margin-top: -5px;
  margin-bottom: -9px;
  font-size: 13px;
}
.info-title {
  color: #3e495c;
  font-size: 13px;
}

.operator-detail-content {
  color: #3e495c;
  font-size: 13px;
}
</style>
