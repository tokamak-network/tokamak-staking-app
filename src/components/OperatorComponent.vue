<template>
  <view
    class="operator-wrap"
    :style="{
      marginBottom: windowHeight * 0.023,
      width: componentWidth,
      paddingLeft: componentWidth * 0.063,
      paddingRight: componentWidth * 0.063,
    }"
  >
    <touchable-opacity :on-press="!pressed ? openOperator : null">
      <view
        class="operator-container"
        :style="{
          width: componentWidth * 0.875,
          marginTop: windowHeight * 0.122 * 0.256,
        }"
      >
        <view>
          <image
            class="operator-img"
            :source="tokamak"
            :style="{
              height: windowHeight * 0.122 * 0.45,
              width: componentWidth * 0.11,
              marginRight: componentWidth * 0.056,
              resizeMode: 'contain',
              opacity:0.12,
            }"
          >
          </image>
        </view>
        <view
          class="operator-subcontainer"
          :style="{ width: componentWidth * 0.7 }"
        >
          <view class="operator-text-container">
            <text class="operator-title">{{ operator.name }}</text>
            <touchable-opacity
              :on-press="
                selectedOperator === operator.name
                  ? openInformation
                  : openOperator
              "
            >
              <text
                :class="{
                  selected: selectedOperator === operator.name,
                  'operator-content': selectedOperator !== operator.name,
                }"
                :style="{
                  marginBottom:
                    selectedOperator !== operator.name
                      ? componentHeight * 0.256
                      : componentHeight * 0.046,
                  height:
                    selectedOperator === operator.name
                      ? componentHeight * 0.038
                      : windowHeight * 0.122 * 0.192,
                }"
                >{{
                  selectedOperator !== operator.name ? info : "More Information"
                }}
              </text>
            </touchable-opacity>
          </view>
          <touchable-opacity
            v-if="pressed && selectedOperator === operator.name"
            :on-press="closeOperator"
          >
            <image
              class="operator-img-close"
              :style="{
                width: componentWidth * 0.075,
                height: componentHeight * 0.069,
              }"
              :source="pressed ? CloseIcon : null"
            >
            </image>
          </touchable-opacity>
        </view>
      </view>
    </touchable-opacity>
    <view
      v-if="pressed && selectedOperator === operator.name"
      class="operator-detail"
    >
      <view class="divider" :style="{ width: componentWidth * 0.875 }" />
      <view
        class="operator-detail-container"
        :style="{ paddingTop: componentHeight * 0.043 }"
      >
        <view
          class="operator-detail-text"
          :style="{ marginBottom: componentHeight * 0.055 }"
        >
          <text class="operator-detail-title">Commission Rate</text>
          <text class="operator-detail-content"
            >{{ operator.isCommissionRateNegative === "1" ? "-" : ""
            }}{{ rateOf(operator.commissionRate) }}</text
          >
        </view>
        <view
          class="operator-detail-text"
          :style="{ marginBottom: componentHeight * 0.055 }"
        >
          <text class="operator-detail-title">Most recent Commit</text>
          <text class="operator-detail-content">{{
            fromNow(operator.lastFinalizedAt)
          }}</text>
        </view>
        <view
          class="operator-detail-text"
          :style="{ marginBottom: componentHeight * 0.055 }"
        >
          <text class="operator-detail-title">My Staked</text>
          <text class="operator-detail-content">{{
            currencyAmount(operator.userStaked)
          }}</text>
        </view>
        <view
          class="divider"
          :style="{
            width: componentWidth * 0.875,
            marginBottom: componentHeight * 0.058,
          }"
        />
        <view
          class="operator-detail-text"
          :style="{ marginBottom: componentHeight * 0.055 }"
        >
          <text class="operator-detail-title">Available Amount</text>
          <text class="operator-detail-content">{{
            currencyAmount(tonBalance)
          }}</text>
        </view>
        <view
          class="operator-detail-input"
          :style="{
            height: componentHeight * 0.104,
            width: componentWidth * 0.875,
          }"
        >
          <text class="info-title">Amount</text>
          <text-input
            class="value-input"
            v-model="amountToDelegate"
            placeholder="0.00"
            autocomplete="off"
            :minLength="1"
            :maxLength="50"
            keyboardType="numeric"
            :style="{ width: componentWidth * 0.6 }"
          />
          <text class="info-title">TON</text>
        </view>
        <touchable-opacity :on-press="onPressButton">
          <button-main
            title="Stake"
            :style="{ marginBottom: componentWidth * 0.043 }"
          />
        </touchable-opacity>
      </view>
    </view>
    <operator-info
      :modalVisible="openOperatorInfo"
      :layer2="operator.layer2"
      @propFromChild="childPropReceived"
    ></operator-info>
    <fee
      :modalVisible="feeModelVisibility"
      @getCustomValues="getCustomValues"
      @closeFeeModel="closeFeeModel"
      :slowPrice="slowPrice"
      :normalPrice="normalPrice"
      :fastPrice="fastPrice"
      :gasLimit="gasLimit"
    ></fee>
    <alert
      :modalVisible="alertVisibility"
      :width="0.889"
      :height="0.242"
      :message="message"
      @closePopup="closePopUp"
    ></alert>
  </view>
</template>
<script>
import Fee from "@/components/Fee";
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
import { Dimensions } from "react-native";
import OperatorInfo from "@/components/OperatorInfo";
import Alert from "@/components/Alert";

export default {
  data() {
    return {
      tokamak,
      dxm,
      dsrv,
      CloseIcon,
      pressed: false,
      opendNow: null,
      amountToDelegate: "0",
      openOper: "",
      openOperatorInfo: false,
      feeModelVisibility: false,
      alertVisibility: false,
      message: "",
      price: 0,
      limit: 0,
      slowPrice: 0,
      normalPrice: 0,
      fastPrice: 0,
      gasLimit: 0,
    };
  },
  components: {
    "button-main": ButtonMain,
    "operator-info": OperatorInfo,
    fee: Fee,
    alert: Alert,
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
      "DepositManager",
      "tonBalance",
      "user",
      "TON",
      "WTON",
      "SeigManager",
      "selectedOperator",
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
      const isNeg = this.operator.isCommissionRateNegative === "1" ? "-" : "";
      const rate = isNeg + this.rateOf(this.operator.commissionRate);
      const lastComm = this.fromNow(this.operator.lastFinalizedAt);
      return commission + rate + " " + committed + lastComm;
    },
    currencyAmount() {
      return (amount) => this.$options.filters.currencyAmount(amount);
    },
    windowWidth() {
      return Dimensions.get("window").width;
    },
    windowHeight() {
      return Dimensions.get("window").height;
    },
    componentHeight() {
      if (this.selectedOperator === this.operator.name) {
        return Dimensions.get("window").height * 0.541;
      } else {
        return Dimensions.get("window").height * 0.122;
      }
    },
    componentWidth() {
      return Dimensions.get("window").width * 0.889;
    },
    hexSlicer() {
      return (address) => this.$options.filters.hexSlicer(address);
    },
  },
  methods: {
    openOperator() {
      this.pressed = true;
      this.$store.dispatch("setOpenOperator", this.operator.name);
    },
    closeOperator() {
      this.pressed = false;
      this.$store.dispatch("setOpenOperator", "");
    },
    closeFeeModel() {
      this.feeModelVisibility = false;
    },
    closePopUp(close) {
      this.alertVisibility = close;
    },
    openInformation() {
      this.openOperatorInfo = true;
    },
    childPropReceived(args1) {
      this.openOperatorInfo = args1;
    },
    async onPressButton() {
      BlockchainModule.getFeeInfo((fast, normal, slow) => {
        this.slowPrice = slow;
        this.normalPrice = normal;
        this.fastPrice = fast;
      });
      if (
        this.amountToDelegate === "0" ||
        parseFloat(this.amountToDelegate) === 0
      ) {
        this.message = "Please input a valid TON amount";
        this.alertVisibility = true;
      } else if (_TON(this.amountToDelegate).gt(this.tonBalance)) {
        this.message = "Please input a valid TON amount";
        this.alertVisibility = true;
      } else {
        const amount = _TON(this.amountToDelegate).toFixed("wei");
        const data = this.getData();
        const gasValue = await BlockchainModule.esitmatedGasLimitForDelegate(
          this.TON,
          "approveAndCall",
          this.WTON,
          amount,
          data
        );
        const gasVal = parseInt(gasValue);
        this.gasLimit = gasVal;
        this.feeModelVisibility = true;
      }
    },
    getCustomValues(price, limit) {
      this.price = price;
      this.limit = limit;

      this.feeModelVisibility = false;
      this.delegate();
    },
    async delegate() {
      const amount = _TON(this.amountToDelegate).toFixed("wei");
      const data = this.getData();
      const status = await BlockchainModule.approveAndCall(
        this.TON,
        "approveAndCall",
        this.price.toString(),
        this.limit.toString(),
        this.WTON,
        amount,
        data
      );
      if (status.code === 0) {
        this.index = 0;
        ToastAndroid.show(
           "Transaction " + `${this.hexSlicer(status.hash)  }` + "successfully added to queue" ,
          ToastAndroid.LONG);
        const transaction = {
          from: this.user,
          type: "Withdrawn",
          amount: amount,
          transactionHash: status.hash,
          target: this.operator.layer2,
        };
        //  this.$store.dispatch('addPendingTransaction', transaction);
        this.$store.dispatch("addPendingTransaction", transaction);
        this.$store.dispatch("setOperators");
        this.$store.dispatch("setBalance");
        this.$store.dispatch("setRounds");
        this.$store.dispatch("setCurrentRound");
        this.$store.dispatch("checkPendingTransactions");
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
  border-width: 1;
  border-color: #e7ebf2;
  border-radius: 10px;
  background-color: #ffffff;
}
.operator-container {
  display: flex;
  flex-direction: row;
}

.operator-title {
  font-size: 17px;
  font-weight: bold;
  color: #131315;
  line-height: 23px;
}
.operator-content {
  font-size: 11px;
  color: #86929d;
  line-height: 15px;
}
.operator-subcontainer {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.selected {
  font-size: 11px;
  color: #2a72e5;
}
.divider {
  /* width: 280px; */
  height: 1px;
  background-color: #e7ebf2;
}
.operator-detail {
  display: flex;
  align-self: center;
}
.operator-detail-text {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  /* border-width: 1px;
  border-color: red; */
}
.operator-detail-input {
  border-width: 1px;
  border-radius: 10px;
  border-color: #dfe4ee;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4.3%;
  padding-left: 3.6%;
  padding-right: 3.6%;
}
.operator-detail-btn {
  width: 280px;
  height: 40px;
  border-radius: 4px;
}
.value-input {
  text-align: right;
  font-size: 13px;
  overflow: hidden;
  color: #555555;
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
