<template>
  <view class="staking-layout">
    <header :navigation="navigation" :back="true" />
    <view class="staking-container">
      <view class="avatar" :style="{ backgroundColor: operator.color }">
        <text class="opr">O P R</text>
      </view>
      <text class="title">{{ operator.name }}</text>
      <view class="info-row">
        <text class="info-title">Commission Rate</text>
        <text class="info-description"
          >{{ operator.isCommissionRateNegative === 1 ? "-" : ""
          }}{{ rateOf(operator.commissionRate) }}</text
        >
      </view>
      <view class="info-row">
        <text class="info-title">Most Recent Commit</text>
        <text class="info-description">{{
          fromNow(operator.lastFinalizedAt)
        }}</text>
      </view>
    
      <view class="amount-container">
        <view class="amount-row">
          <text class="info-title">Available Amount</text>
          <text class="info-description">{{ currencyAmount(tonBalance) }}</text>
        </view>
        <view
          class="amount-row"
          :style="{ backgroundColor: '#FFFFFF', padding: 8, borderRadius: 13 }"
        >
          <text class="info-title">Amount</text>
          <view class="input-text">
            <text-input
              class="value-input"
              v-model="amountToDelegate"
              placeholder="0.00"
              autocomplete="off"
              minlength="1"
              maxlength="1"
              keyboardType="numeric"
            />
            <text class="info-description">TON</text>
          </view>
        </view>
        <touchable-opacity
          :on-press="onPressButton"
          :style="{ width: 320, marginTop: 10 }"
        >
          <button-main title="Stake" />
        </touchable-opacity>
      </view>
    </view>
  </view>
</template>
<script>
import { ToastAndroid } from "react-native";
import Header from "@/components/Header";
import ButtonMain from "@/components/ButtonMain";
import { Button } from "react-native";
const _TON = createCurrency("TON");
const _WTON = createCurrency("WTON");
import { BN, padLeft } from "web3-utils";
import { mapState, mapGetters } from "vuex";
import { createCurrency } from "@makerdao/currency";
import { getConfig } from "../../config.js";
import { NativeModules } from "react-native";
const { BlockchainModule } = NativeModules;
export default {
  components: {
    header: Header,
    "button-main": ButtonMain,
  },
  data() {
    return {
      layer2: "",
      selectedOperator: "",
      amount: "",
      amountToDelegate: "",
      index: 0,
      nav: {
        type: Object,
      },
    };
  },
  created() {
    this.layer2 = this.navigation.state.params.layer2;
    this.nav = this.navigation.state.params.navigation;
  },
  computed: {
    ...mapState([
      "operators",
      "tonBalance",
      "blockNumber",
      "user",
      "TON",
      "WTON",
      "DepositManager",
      "SeigManager",
    ]),
    ...mapGetters(["operatorByLayer2"]),
    operator() {
      return this.operatorByLayer2(this.layer2);
    },
    filteredImgURL() {
      return (name) =>
        name !== "" ? `${getConfig().baseURL}/avatars/${name}` : "";
    },
    currencyAmount() {
      return (amount) => this.$options.filters.currencyAmount(amount);
    },
    fromNow() {
      return (timestamp, suffix = false) =>
        this.$options.filters.fromNow(timestamp, suffix);
    },
    rateOf() {
      return (commissionRate) => this.$options.filters.rateOf(commissionRate);
    },
    minimumAmount() {
      return this.SeigManager.methods.minimumAmount().call();
    },
    operatorMinimumAmount() {
      const operatorDeposit = this.operator.selfDeposit;
      const minimumAmount = this.operator.minimumAmount;
      const lessThan = operatorDeposit < minimumAmount;
      if (this.user !== this.operator.address) {
        return lessThan;
      } else {
        return false;
      }
    },
  },
  props: {
    navigation: {
      type: Object,
    },
    route: {
      type: Object,
    },
  },

  methods: {
    async onPressButton() {
      if (
        this.amountToDelegate === "" ||
        parseFloat(this.amountToDelegate) === 0
      ) {
        return alert("Please check your TON amount.");
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
            type: 'Withdrawn',
            amount: amount,
            transactionHash: status.hash,
            target: this.operator.layer2,
        }
        //  this.$store.dispatch('addPendingTransaction', transaction);
           this.$store.dispatch('setBalance');
          this.amountToDelegate = '';
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
.staking-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.staking-container {
  flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding-top: 20;
}
.title {
  color: #8c8c8c;
  font-size: 28px;
  padding-bottom: 20px;
  color: #555555;
  font-weight: 700;
}
.avatar {
  display: flex;
  width: 65px;
  height: 65px;
  font-size: 30px;
  font-weight: 700;
  border-radius: 50;
  justify-content: center;
  align-items: center;
}
.opr {
  color: white;
  font-size: 20px;
  font-weight: 600;
}
.info-row {
  padding-top: 10px;
  padding-left: 30px;
  padding-right: 30px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-self: stretch;
  align-items: center;
  position: relative;
}
.info-title {
  color: #555555;
  font-size: 18px;
}
.info-description {
  color: #555555;
  font-size: 18px;
  font-weight: 700;
}
.amount-container {
  width: 85%;
  background-color: #e2e8eb;
  border-width: 1;
  border-color: #ccd1d3;
  border-radius: 13;
  height: 195px;
  padding: 15px;
  margin: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.amount-row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-bottom: 10px;
  height: 40px;
  align-self: stretch;
  align-items: center;
  position: relative;
  width: 100%;
}
.input-text {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-self: stretch;
  align-items: center;
  position: relative;
  height: 25px;
}
.value-input {
  text-align: right;
  height: 38px;
  font-size: 18px;
  width: 20px;
  overflow: hidden;
  width: 120px;
  margin-right: 20px;
  align-items: center;
  color: #555555;
  margin-top: -5px;
  margin-bottom: -9px;
}
</style>
