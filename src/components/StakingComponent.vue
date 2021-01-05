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
        <text class="total-balance">Balance: 99.67 TON</text>
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
          >67.77</text
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
        <text class="total-balance">Balance: 232.23 TON</text>
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
          >55353467.77</text
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
import ButtonMain from "@/components/ButtonMain";
import { ActionSheet } from "native-base";

export default {
  data() {
    return {
      activeTab: "Stake",
      amountToDelegate: "",
      amountToUndelegate: "",
      selectedOperator: "tokamak1",
      operators: [
        { name: "tokamak1", color: "#b23756" },
        { name: "DXM Corp", color: "#8948a2" },
        { name: "DSRV", color: "#78eef2" },
      ],
    };
  },
  components: {
    "button-main": ButtonMain,
  },
  methods: {
    changeTab(tab) {
      this.activeTab = tab;
    },
    delegate() {
      console.log("delegate");
    },
    undelegate() {
      console.log("undelegate");
    },
    redelegate() {
      console.log("redelegate");
    },
    withdraw() {
      console.log("withdraw");
    },
    setAvailableAmountToDelegate() {
      this.amountToDelegate = "99.67";
    },
    setAvailableAmountToUndelegate () {
        this.amountToUndelegate = "232.23"
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
          if (buttonIndex === undefined){
            this.selectedOperator = this.selectedOperator;
          }
          else{
            this.selectedOperator = ops[buttonIndex];
          }
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
