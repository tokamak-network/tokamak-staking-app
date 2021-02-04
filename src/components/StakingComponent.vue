<template>
  <view class="staking-component-container">

    <view class="button-container">
      <!-- <animatedView> -->
      <touchable-opacity
        class="button-comp"
        :on-press="() => changeTab('Stake')"
      >
        <text :class="{selected: activeTab === 'Stake', 'button-name': activeTab !== 'Stake'}"
          >Stake</text
        >
      </touchable-opacity>
      <!-- </animatedView> -->
      <touchable-opacity
        class="button-comp"
        :on-press="() => changeTab('Unstake')"
      >
        <text :class="{selected: activeTab === 'Unstake', 'button-name': activeTab !== 'Unstake'}"
          >Unstake</text
        >
      </touchable-opacity>
      <touchable-opacity
        class="button-comp"
        :on-press="() => changeTab('Withdraw')"
      >
        <text
          :class="{selected: activeTab === 'Withdraw', 'button-name': activeTab !== 'Withdraw'}"
          >Withdraw</text       
        >
      </touchable-opacity>
    </view>

    <view v-if="activeTab === 'Stake'" class="value-wrap" :style="{width: windowWidth*styles.valueWrapWidth, height: windowHeight*styles.valueWrapHeight}">
      <view class="value-row value-row-first" :style="{height: windowHeight*styles.valueBalanceFirstHeight}">
        <text class="value-row-text">Balance : </text>
        <text class="value-row-value">4,598.24 TON</text>
      </view>
      <view class="value-row value-row-sb" :style="{height: windowHeight*styles.valueBalanceFirstHeight, marginBottom: '3%'}">
        <text-input class="value-row-second-input" placeholder="0.00"></text-input>
        <touchable-opacity
            class="value-row-second-touch"
            :on-press="() => setAvailableAmountToUndelegate()"
          >
            <text class="value-row-second-max" :style="{width: windowWidth*styles.valueBalanceSecondMax}"
            >MAX</text>
        </touchable-opacity>
      </view>
      <view
      class="value-row value-row-third value-row-border"
      :style="{height: windowHeight*styles.valueRowThirdheight}"
      >
         <text 
         class="value-row-third-text"
         
          >Select an operator </text
        >
        <image class="value-icon" :style="{height: windowHeight*0.031, width: windowWidth*0.056, resizeMode: 'contain'}" :source=IconTokamak></image>
        <text 
        class="value-row-thrid-select"
        :onPress="handleBtnPress">{{selectedOperator}}</text>
        <image :style="{height: windowHeight*0.012, width: windowWidth*0.025, resizeMode: 'contain'}" :source=IconSelect></image>
      </view> 
      <button-main title="Stake" :style="{marginBottom: '4.5%'}"></button-main>
      <divider :style="{marginBottom: '4.5%'}"></divider>
      <text-input class="value-row-border" :style="{height: windowHeight*styles.valueRowThirdheight, marginBottom: '4.5%'}" placeholder="0.00"></text-input>
      <button-main title="Re-Stake"></button-main>
    </view>

    <view v-if="activeTab === 'Unstake'" class="value-wrap" :style="{width: windowWidth*styles.valueWrapWidth, height: windowHeight*0.31}">
      <view class="value-row value-row-first" :style="{height: windowHeight*styles.valueBalanceFirstHeight}">
        <text class="value-row-text">Balance : </text>
        <text class="value-row-value">4,598.24 TON</text>
      </view>
      <view class="value-row value-row-sb" :style="{height: windowHeight*styles.valueBalanceFirstHeight, marginBottom: '3%'}">
        <text-input class="value-row-second-input" placeholder="0.00"></text-input>
        <touchable-opacity
            class="value-row-second-touch"
            :on-press="() => setAvailableAmountToUndelegate()"
          >
            <text class="value-row-second-max" :style="{width: windowWidth*styles.valueBalanceSecondMax}"
            >MAX</text>
        </touchable-opacity>
      </view>
      <view
      class="value-row value-row-third value-row-border"
      :style="{height: windowHeight*styles.valueRowThirdheight}"
      >
         <text 
         class="value-row-third-text"
         
          >Select an operator </text
        >
        <image class="value-icon" :style="{height: windowHeight*0.031, width: windowWidth*0.056, resizeMode: 'contain'}" :source=IconTokamak></image>
        <text 
        class="value-row-thrid-select"
        :onPress="handleBtnPress">{{selectedOperator}}</text>
        <image :style="{height: windowHeight*0.024, width: windowWidth*0.025, resizeMode: 'contain'}" :source=IconSelect></image>
      </view> 
      <button-main title="Unstake"></button-main>
    </view>

    <view v-if="activeTab === 'Withdraw'" class="value-wrap" :style="{width: windowWidth*styles.valueWrapWidth, height: windowHeight*0.31}">
      <view class="value-row value-row-first" :style="{height: windowHeight*styles.valueBalanceFirstHeight}">
        <text class="value-row-text">Widhdrawable Amount : </text>
      </view>
      <view class="value-row value-row-sb" :style="{height: windowHeight*styles.valueBalanceFirstHeight, marginBottom: '3%'}">
        <text-input class="withdraw-value-row-input" :style="{height: windowHeight*0.062}" placeholder="0.00"><text>TON</text></text-input>
      </view> 
      <view
      class="value-row value-row-third value-row-border"
      :style="{height: windowHeight*styles.valueRowThirdheight}"
      >
         <text 
         class="value-row-third-text"
          >Select an operator </text
        >
        <image class="value-icon" :style="{height: windowHeight*0.031, width: windowWidth*0.056, resizeMode: 'contain'}" :source=IconTokamak></image>
        <text 
        class="value-row-thrid-select"
        :onPress="handleBtnPress">{{selectedOperator}}</text>
        <image :style="{height: windowHeight*0.024, width: windowWidth*0.025, resizeMode: 'contain'}" :source=IconSelect></image>
      </view> 
      <button-main title="Withdraw"></button-main>
    </view>

    <alert :modalVisible=true :width=0.889 :height=0.242></alert>
    <select-operator :modalVisible=false></select-operator>
  </view>
</template>

<script>
import ButtonMain from "@/components/ButtonMain";
import Divider from "@/components/Divider"
import Alert from "@/components/Alert"
import SelectOperator from "@/components/SelectOperator"
import IconTokamak from "../../assets/tokamak.png"
import IconSelect from "../../assets/select.png"
import { ActionSheet } from "native-base";
import { Dimensions } from 'react-native';

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
      styles: {
        valueWrapWidth : 0.889,
        valueWrapHeight : 0.48,
        valueBalanceFirstHeight : 0.055,
        valueBalanceSecondMax : 0.22,
        valueRowThirdheight: 0.062,
      },
      IconTokamak,
      IconSelect
    };
  },
  components: {
    "button-main": ButtonMain,
    "divider": Divider,
    "alert": Alert,
    "select-operator": SelectOperator
  },
  computed: {
   windowWidth () {
      return Dimensions.get('window').width
    },
    windowHeight () {
      return Dimensions.get('window').height
    },
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
          options: [
             { text: "Option 0", icon: "tokamak", iconColor: "black" },
        { text: "Option 1", icon: "analytics", iconColor: "#f42ced" },
        { text: "Option 2", icon: "aperture", iconColor: "#ea943b" },
        { text: "Delete", icon: "trash", iconColor: "#fa213b" },
        { text: "Cancel", icon: "close", iconColor: "#25de5b" }

          ],
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
    },
  },
};
</script>
<style scoped>
.staking-component-container {
  width: 100%;
  background-color: #ffffff;
  height: 100%;
}

.value-wrap {
  border-width: 1px;
  border-color: #e7ebf2;
  border-radius: 10px;
  padding-left: 6.2%;
  padding-right: 6.2%;
  padding-top: 3%;
  display: flex;
  flex-direction: column;
}

.value-row {
  display: flex;
  flex-direction: row;
}

.value-row-first {
  align-items: center;
}

.value-row-sb {
  justify-content: space-between;
  align-items: center;
}

.value-row-text {
  font-size: 12px;
  color: #86929d;
}

.value-row-value {
  font-size: 13px;
  color: #3e495c;
}

.value-row-second-input {
  width: 67.4%;
  height: 100%;
  border-width: 1px;
  border-radius: 4px;
  border-color: #dfe4ee;
}

.value-row-second-touch {
  height: 100%;
  border-width: 1px;
  border-color: #2a72e5;
  border-radius: 4px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.value-row-second-max {
  text-align: center;
  color: #2a72e5;
}

.value-row-border {
  border-width: 1px;
  border-radius: 4px;
  border-color: #dfe4ee;
}

.value-row-third {
  margin-bottom: 4.5%;
  align-items: center;
}

.value-row-third-text {
  padding-left: 3.6%;
  font-size: 12px;
  color: #3e495c;
  margin-right: 17.9%;
  font-size: 12px;
}

.value-icon {
  margin-right: 2.5%;
}

.value-row-thrid-select {
  font-weight: bold;
  font-size: 13px;
  color: #3e495c;
  margin-right: 2%;
}

.button-container {
  width: 88.9%;
  height: 5.6%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-bottom: 3.9%;
  border-width: 1px;
  border-color: #e7ebf2;
  border-radius: 6px;
  padding-left: 0.9%;
  padding-right: 0.9%;
}
.button-comp {
  display: flex;
  width: 33%;
  justify-content: center;
  align-self: stretch;
  align-items: center;
  position: relative;
  color: #818992;
}
.button-name {
  color: #818992;
  font-weight: bold;
}

.selected {
  width: 100%;
  height: 90%;
  text-align: center;
  padding-top: 9%;
  border-radius: 5px;
  color: #ffffff;
  background-color: #2a72e5;
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
.withdraw-value-row-input {
  width: 100%;
  border-width: 1px;
  border-color: #dfe4ee;
  border-radius: 4px;
  text-align: right;
  padding-right: 3.6%;
  background-color: #e9edf1;
}
</style>
