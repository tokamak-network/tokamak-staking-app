<template>
  <view class="staking-component-container">
    <view class="button-container">
      <touchable-opacity
        class="button-comp"
        :on-press="() => changeTab('Stake')"
      >
        <text class="button-name" :class="{ selected: activeTab === 'Stake' }"
          >Stake</text
        >
      </touchable-opacity>
      <touchable-opacity
        class="button-comp"
        :on-press="() => changeTab('Unstake')"
      >
        <text class="button-name" :class="{ selected: activeTab === 'Unstake' }"
          >Unstake</text
        >
      </touchable-opacity>
      <touchable-opacity
        class="button-comp"
        :on-press="() => changeTab('Withdraw')"
      >
        <text
          class="button-name"
          :class="{ selected: activeTab === 'Withdraw' }"
          >Withdraw</text
        >
      </touchable-opacity>
    </view>

    <view v-if="activeTab === 'Stake'">
      <view class="value-container">
        <text class="total-balance">TON Balance: {{ currencyAmount(tonBalance) }}</text>
        <view class="main-row">
          <text-input
            class="input"
            v-model="amountToDelegate"
            placeholder="0.00"
            autocomplete="off"
            minlength="1"
            maxlength="1"
            keyboardType="numeric"
          />
          <touchable-opacity
            class="button-max"
            :on-press="() => setAvailableAmountToDelegate()"
          >
            <text class="button-name">MAX</text>
          </touchable-opacity>
          <image
            class="logo"
            :style="{ width: 39, height: 25, margin: 5 }"
            :source="require('../../assets/TokamakLogo.png')"
          />
          <text class="button-name">TON</text>
        </view>
      </view>
      <view
        class="value-container"
        :style="{ height: 50, flexDirection: 'row' }"
      >
        <text :style="{ fontSize: 17, marginTop: 4, color: '#555555' }" :onPress="handleBtnPress"
          >Select an operator ▼</text
        >
        <text class="selectedOperator">{{selectedOperator}}</text>
      </view>
      <touchable-opacity :on-press="delegate" :style="{ marginBottom: 15 }">
        <button-main title="Stake" />
      </touchable-opacity>
      <view
        class="value-container"
        :style="{ height: 50, flexDirection: 'row', alignItems: 'center' }"
      >
        <text :style="{ fontSize: 16, marginTop: 4, color: '#555555', marginRight: 10 }"
          >Re-stake Amount:</text
        >
        <text :style="{ fontSize: 16, marginTop: 4, color: '#555555', marginRight: 10, width:60, textAlign: 'right' }"
          >{{selectedOperator===""?"":currencyAmount(operator.userNotWithdrawable).toString().replace("TON", "")}}</text
        >
        <image
            class="logo"
            :style="{ width: 39, height: 25, margin: 5 }"
            :source="require('../../assets/TokamakLogo.png')"
          />
          <text class="button-name">TON</text>
      </view>
      <touchable-opacity :on-press="redelegate" :style="{ marginBottom: 15 }">
        <button-main title="Re-stake" />
      </touchable-opacity>
    </view>
    <view v-if="activeTab === 'Unstake'">
      <view class="value-container">
        <text class="total-balance">{{selectedOperator===""?"":currencyAmount(operator.userStaked)}}</text>
        <view class="main-row">
          <text-input
            class="input"
            v-model="amountToUndelegate"
            placeholder="0.00"
            autocomplete="off"
            minlength="1"
            maxlength="1"
            keyboardType="numeric"
          />
          <touchable-opacity
            class="button-max"
            :on-press="() => setAvailableAmountToUndelegate()"
          >
            <text class="button-name">MAX</text>
          </touchable-opacity>
          <image
            class="logo"
            :style="{ width: 39, height: 25, margin: 5 }"
            :source="require('../../assets/TokamakLogo.png')"
          />
          <text class="button-name">TON</text>
        </view>
      </view>
      <view
        class="value-container"
        :style="{ height: 50, flexDirection: 'row' }"
      >
         <text :style="{ fontSize: 17, marginTop: 4, color: '#555555' }" :onPress="handleBtnPress"
          >Select an operator ▼</text
        >
        <text class="selectedOperator">{{selectedOperator}}</text>
      </view>
      <touchable-opacity :on-press="undelegate" :style="{ marginBottom: 15 }">
        <button-main title="Unstake" />
      </touchable-opacity>
    </view>
    <view v-if="activeTab === 'Withdraw'">
     <view
        class="value-container"
        :style="{ height: 60, flexDirection: 'row', alignItems: 'center' }"
      >
        <text :style="{ fontSize: 16, marginTop: 4, color: '#555555', marginRight: 10, width: 90 }"
          >Withdrawable Amount:</text
        >
        <text :style="{ fontSize: 16, marginTop: 4, color: '#555555', marginRight: 10, width:90, textAlign: 'right' }"
          > {{
           selectedOperator===""?"": currencyAmount(operator.userWithdrawable)
              .toString()
              .replace("TON", "")
          }}</text
        > 
        <image
            class="logo"
            :style="{ width: 39, height: 25, margin: 5 }"
            :source="require('../../assets/TokamakLogo.png')"
          />
          <text class="button-name">TON</text>
      </view>
      <view
        class="value-container"
        :style="{ height: 50, flexDirection: 'row' }"
      >
         <text :style="{ fontSize: 17, marginTop: 4, color: '#555555' }" :onPress="handleBtnPress"
          >Select an operator ▼</text
        >
        <text class="selectedOperator">{{selectedOperator}}</text>
      </view>
      <touchable-opacity :on-press="withdraw" :style="{ marginBottom: 15 }">
        <button-main title="Withdraw" />
      </touchable-opacity>
    </view>
  </view>
</template>

<script>
import { ToastAndroid } from "react-native";

import ButtonMain from "@/components/ButtonMain";
import { ActionSheet } from "native-base";
import { NativeModules } from "react-native";
const { BlockchainModule } = NativeModules;
import { createCurrency } from "@makerdao/currency";
import { BN, padLeft } from 'web3-utils';
import { range } from 'lodash';

import { mapState, mapGetters } from 'vuex';
const _TON = createCurrency("TON");
const _WTON = createCurrency("WTON");

export default {
  data() {
    return {
      activeTab: "Stake",
       layer2: '',
      selectedOperator: '',
      amount: '',
      amountToDelegate: '',
      amountToUndelegate: '',
      index: 0,
    };
  },
  components: {
    "button-main": ButtonMain,
  },
  computed: {
    ...mapState([
      'operators',
      'tonBalance',
      'web3',
      'blockNumber',
      'user',
      'TON',
      'WTON',
      'DepositManager',
      'SeigManager',
    ]),
    ...mapGetters(['operatorByLayer2']),
    operator () {
      return this.operatorByLayer2(this.layer2);
    },
    currencyAmount () {
      return (amount) => this.$options.filters.currencyAmount(amount);
    },
    notWithdrawableMessage () {
      return (withdrawableBlockNumber) =>
        `You have to wait for ${withdrawableBlockNumber -
          this.blockNumber} blocks to withdraw all the tokens.`;
    },

    withdrawableRequests () {
      return this.operator.withdrawalRequests.length;
    },
    redelegatableRequests () {
      return this.operator.withdrawalRequests.length - this.index;
    },
    redelegatableAmount () {
      let amount = new BN(0);
      for (const i of range(this.redelegatableRequests)) {
        amount = amount.add(new BN(this.operator.withdrawalRequests[i].amount));
      }
      return _WTON(amount.toString(), 'ray');
    },
    disableButton () {
      return false;
    },
    minimumAmount () {
      return this.SeigManager.methods.minimumAmount().call();
    },
    operatorMinimumAmount () {
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
  methods: {
    changeTab(tab) {
      this.activeTab = tab;
    },
    async delegate() {
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
            this.$store.dispatch('setOperators');
          this.amountToDelegate = '';
      } else {
        ToastAndroid.show("Transaction Unsuccessful", ToastAndroid.SHORT);
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
    async undelegate() {
      if (
        this.amountToUndelegate === '' ||
        parseFloat(this.amountToUndelegate) === 0
      ) {
        return alert('Please check input amount.');
      }
      if (_WTON(this.amountToUndelegate).gt(this.operator.userStaked)) {
        return alert('Please check your TON amount.');
      }
      const amount = _WTON(this.amountToUndelegate).toFixed('ray');
      const status = await BlockchainModule.requestWithdrawal(
        this.DepositManager,
        "requestWithdrawal",
       this.operator.layer2,
        amount,
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
            this.$store.dispatch('setOperators');
          this.amountToUndelegate = '';
      } else {
        ToastAndroid.show("Transaction Unsuccessful", ToastAndroid.SHORT);
      }
    },
    redelegate() {
      console.log("redelegate");
    },
    withdraw() {
      console.log("withdraw");
    },
    setAvailableAmountToDelegate() {
      const tonAmount = this.tonBalance.toBigNumber().toString();
      const index = tonAmount.indexOf('.');
      if (index === -1) {
        this.amountToDelegate = tonAmount + '.00';
      } else {
        this.amountToDelegate = tonAmount;
      }
    },
    setAvailableAmountToUndelegate () {  
      const tonAmount = this.operator.userStaked.toBigNumber().toString();
      const index = tonAmount.indexOf('.');
      if (index === -1) {
        this.amountToUndelegate = tonAmount + '.00';
      } else {
        this.amountToUndelegate = tonAmount;
      }
    },
    onValueChange (value) {
        this.selectedOperator = value;
    },
    handleBtnPress (){
      const ops = this.operators.map(op => op.name);
      ActionSheet.show(
        {
          options: ops,
          title: "Select an operator"
        },
        buttonIndex => {
           const operator = this.operators.find(
        (operator) => operator.name ===  ops[buttonIndex]
      );
      const root = operator.layer2;
      this.layer2 = root;
          this.selectedOperator = ops[buttonIndex];
        }
      )
    }
  },
};
</script>
<style scoped>
.staking-component-container {
  width: 90%;
  background-color: #e2e8eb;
  margin-left: 20px;
  margin-right: 20px;
  margin-top: 20px;
  height: 400px;
  border-width: 1;
  border-color: #ccd1d3;
  border-radius: 13;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding: 10px;
}
.value-container {
  width: 300px;
  height: 80px;
  display: flex;
  padding: 10px;
  border-width: 1;
  border-color: #ccd1d3;
  border-radius: 13;
  flex-direction: column;
  margin-bottom: 15px;
}
.button-container {
  /* width: 100%; */
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-bottom: 25px;
}
.button-comp {
  display: flex;
  width: 33%;
  justify-content: center;
  align-self: stretch;
  align-items: center;
  position: relative;
}
.button-name {
  display: flex;
  font-size: 18px;
  font-weight: bold;
}

.selected {
  opacity: 0.5;
}
.total-balance {
  display: flex;
  align-self: flex-end;
  font-size: 15px;
  margin-bottom: 5px;
}
.main-row {
  display: flex;
  flex-direction: row;
  align-items: center;
}
.input {
  text-align: right;
  height: 30px;
  font-size: 18px;
  width: 120px;
  overflow: hidden;
  margin-right: 5px;
  align-items: center;
  color: #555555;
  background-color: #ffffff;
  border-radius: 8px;
  padding: 5px;
  display: flex;
}
.button-max {
  background-color: #ccd1d3;
  opacity: 0.5;
  width: 60px;
  display: flex;
  justify-content: center;
  align-self: stretch;
  align-items: center;
  position: relative;
  height: 30px;
  margin: 5px;
  border-width: 1;
  border-color: #ccd1d3;
  border-radius: 12px;
}
.dropdown {
  width: 70px;
  height: 20px;
  margin-top: 4px;
}
.dropdown-item {
  height: 10px;
  width: 70px;
  font-weight: bold;
}
.selectedOperator {
  padding-top: 3px;
  margin-left: 30px;
  width: 100px;
  font-weight: bold;
    font-size: 18px;

}
</style>
