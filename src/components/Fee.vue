<template>
    <Modal
    :transparent='true'
    :visible=modalVisible
    >
    <view class="modal-background">
        <view v-if="activeTab === 'selectFee'" class="modal-container" :style="{height: windowHeight, width: windowWidth, top: windowHeight * 0.270}">
          <view class="modal-top">
              <text class="modal-top-text">Fee</text>
              <touchable-opacity :on-press="()=>close()">
              <image :source=CloseIcon :style="{width: windowWidth*0.080, height: windowHeight*0.032, resizeMode: 'contain'}"></image>
              </touchable-opacity>
          </view>
          <touchable-opacity :on-press="()=>selectType('faster', 0.004368)">
          <view :class="{'modal-fee-container' : selectState !== 'faster', 'selected' : selectState === 'faster'}" :style="{height: windowHeight*0.109}">
              <view class="modal-fee-type">
                  <view class="modal-fee-radio" :style="{width: windowWidth * 0.05, height: windowHeight * 0.025, backgroundColor: selectState === 'faster' ?  '#2a72e5' : '#FFFFFF'}">
                      <view class="modal-fee-radio-dot" :style="{width: windowWidth * 0.018, height: windowHeight * 0.010}"></view>
                  </view>
                  <text class="modal-fee-type-text">Faster</text>
              </view>
              <view class="modal-fee-amount">
                  <text class="modal-fee-num">0.004368</text>
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          </touchable-opacity>
          <touchable-opacity :on-press="()=>selectType('normal', 0.003486)">
          <view :class="{'modal-fee-container' : selectState !== 'normal', 'selected' : selectState === 'normal'}" :style="{height: windowHeight*0.109}">
              <view class="modal-fee-type">
                  <view class="modal-fee-radio" :style="{width: windowWidth * 0.05, height: windowHeight * 0.025, backgroundColor: selectState === 'normal' ?  '#2a72e5' : '#FFFFFF'}">
                      <view class="modal-fee-radio-dot" :style="{width: windowWidth * 0.018, height: windowHeight * 0.010}"></view>
                  </view>
                  <text class="modal-fee-type-text">Normal</text>
              </view>
              <view class="modal-fee-amount">
                  <text class="modal-fee-num">0.003486</text>
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          </touchable-opacity>
          <touchable-opacity :on-press="()=>selectType('slower', 0.003049)">
          <view :class="{'modal-fee-container' : selectState !== 'slower', 'selected' : selectState === 'slower'}" :style="{height: windowHeight*0.109}">
              <view class="modal-fee-type">
                  <view class="modal-fee-radio" :style="{width: windowWidth * 0.05, height: windowHeight * 0.025, backgroundColor: selectState === 'slower' ?  '#2a72e5' : '#FFFFFF'}">
                      <view class="modal-fee-radio-dot" :style="{width: windowWidth * 0.018, height: windowHeight * 0.010}"></view>
                  </view>
                  <text class="modal-fee-type-text">Slower</text>
              </view>
              <view class="modal-fee-amount">
                  <text class="modal-fee-num">0.003049</text>
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          </touchable-opacity>
          <touchable-opacity :on-press="()=>goToCustomFee()">
          <view :class="{'modal-fee-container' : selectState !== 'custom', 'selected' : selectState === 'custom'}" :style="{height: windowHeight*0.109}">
              <view class="modal-fee-type">
                  <view class="modal-fee-radio" :style="{width: windowWidth * 0.05, height: windowHeight * 0.025, backgroundColor: selectState === 'custom' ?  '#2a72e5' : '#FFFFFF'}">
                      <view class="modal-fee-radio-dot" :style="{width: windowWidth * 0.018, height: windowHeight * 0.010}"></view>
                  </view>
                  <text class="modal-fee-type-text">Custom</text>
              </view>
              <view class="modal-fee-amount">
                   <text class="modal-fee-num">{{(price*limit*0.000000001).toFixed(6)}}</text>
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          </touchable-opacity>
          <view :style="{marginTop: '1.7%', marginBottom: '2.8%'}">
          <divider></divider>
          </view>
          <view class='modal-fee-total'>
              <text class="modal-fee-text">Total</text>
              <view class="modal-fee-amount modal-fee-amount-last">
                  <text class="modal-fee-num">{{total.toFixed(6)}}</text>
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          <touchable-opacity :on-press="()=>sendCustomValues()">
          <button v-if="btnAble" title="Next"></button>
          <button v-else :style="{backgroundColor: '#e9edf1'}" fontColor="#86929d" title="Next"></button>
          </touchable-opacity>
        </view> 
        <custom-fee v-if="selectState === 'custom' && activeTab === 'customFee'" :selectState=selectState :activeTab=activeTab @propFromChild="childPropReceived" @getCustomValue="setCustomValue"></custom-fee>
    </view>
    </Modal>
</template>

<script>
import {
  Dimensions,
  Modal
} from "react-native";
import Divider from "@/components/Divider"
import ButtonMain from "@/components/ButtonMain"
import CustomFee from "@/components/CustomFee"
import CloseIcon from "../../assets/icon-close.png";

export default {
    data() {
        return{
           CloseIcon,
           selectState : "",
           activeTab : 'selectFee',
           total:0.00,
           price: 0.23,
           limit:0,
           btnAble: false
        }
    },
    props: {
        modalVisible: {
            type: Boolean,
            default: false
        },
    },
    components: {
        "divider": Divider,
        "button": ButtonMain,
        "custom-fee": CustomFee
    },
    methods: {
        selectType(args, value) {
            this.selectState = args;
            this.total = value;
            this.btnNotAble = true
        },
        close() {
            this.modalVisible = false
            this.$emit('propFromChild', this.modalVisible)
        },
        sendPropToParent() {
            this.$emit('propFromChild', this.modalVisible)
        },
        goToCustomFee() {
             this.selectState  = 'custom'
            this.activeTab = 'customFee'
        },
        childPropReceived(args1, args2) {
            this.activeTab = args1;
            this.selectState = args2;
        },
        setCustomValue(state, tab, price, limit){
            console.log(state, tab, price, limit);
            this.activeTab = 'selectFee';
            this.selectState = 'custom';
            this.price = price;
            this.limit = limit
            this.total = price * limit * 0.000000001
        }, 
        sendCustomValues (){
            this.$emit('getCustomValues', this.price, this.limit);
        }
    },
    computed: {
         windowWidth() {
            return Dimensions.get("window").width;
        },
            windowHeight() {
            return Dimensions.get("window").height;
        },
    }
}
</script>
<style>
.modal-background {
   background-color:rgba( 0, 0, 0, 0.45 );
}
.modal-container {
    background-color: #FFFFFF;
    border-width: 1px;
    border-color: #ffffff;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    display: flex;
    flex-direction: column;
    padding-top: 5%;
    padding-left: 5.6%;
    padding-right: 5.6%;
    padding-bottom: 2.8%;
}
.modal-top {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 4.1%;
}
.modal-top-text {
    font-size: 16px;
    color: #131315;
}
.modal-fee-container {
    border-width: 1px;
    border-color: #e7ebf2;
    border-radius: 10px;
    margin-bottom: 2.8%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    padding-left: 4.7%;
    padding-right: 4.7%;
}
.modal-fee-type {
    display: flex;
    flex-direction: row;
    align-items: center;
}
.modal-fee-radio {
    display: flex;
    align-items: center;
    justify-content: center;
    border-width: 1px;
    border-color: #e7ebf2;
    border-radius: 20px;
    margin-right: 10%;
    padding: 5px;
}
.modal-fee-radio-dot {
    border-radius: 10px;
    background-color: #FFFFFF;
}
.modal-fee-type-text {
    font-size: 16px;
    color: #3e495c;
}
.modal-fee-amount {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-end;
}
.modal-fee-amount-last {
    padding-right: 5%;
}
.modal-fee-num {
    font-size: 16px;
    color: #2a72e5;
    margin-right: 5%;
}
.modal-fee-unit {
    font-size: 16px;
    color: #2a72e5;
}
.modal-fee-total {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 7.4%;
}
.selected {
    border-color: #2a72e5;
    border-width: 1px;
    border-radius: 10px;
    margin-bottom: 2.8%;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    padding-left: 4.7%;
    padding-right: 4.7%;
}
.modal-fee-text {
    font-size: 16px;
    color: #3e495c;
}
</style>
