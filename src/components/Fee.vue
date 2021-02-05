<template>
    <Modal
    :transparent='true'
    :visible=modalVisible
    >
    <view class="modal-background">
        <view class="modal-container" :style="{height: windowHeight*0.730, width: windowWidth, top: windowHeight * 0.270}">
          <view class="modal-top">
              <text class="modal-top-text">Fee</text>
              <image :source=CloseIcon :style="{width: windowWidth*0.080, height: windowHeight*0.032, resizeMode: 'contain'}"></image>
          </view>
          <touchable-opacity :on-press="()=>selectType('faster')">
          <view :class="{'modal-fee-container' : selectState !== 'faster', 'selected' : selectState === 'faster'}" :style="{height: windowHeight*0.109}">
              <view class="modal-fee-type">
                  <text class="modal-fee-radio">btn</text>
                  <text class="modal-fee-type-text">Faster</text>
              </view>
              <view class="modal-fee-amount">
                  <text class="modal-fee-num">0.004368</text>
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          </touchable-opacity>
          <touchable-opacity :on-press="()=>selectType('normal')">
          <view :class="{'modal-fee-container' : selectState !== 'normal', 'selected' : selectState === 'normal'}" :style="{height: windowHeight*0.109}">
              <view class="modal-fee-type">
                  <text class="modal-fee-radio">btn</text>
                  <text class="modal-fee-type-text">Normal</text>
              </view>
              <view class="modal-fee-amount">
                  <text class="modal-fee-num">0.003486</text>
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          </touchable-opacity>
          <touchable-opacity :on-press="()=>selectType('slower')">
          <view :class="{'modal-fee-container' : selectState !== 'slower', 'selected' : selectState === 'slower'}" :style="{height: windowHeight*0.109}">
              <view class="modal-fee-type">
                  <text class="modal-fee-radio">btn</text>
                  <text class="modal-fee-type-text">Slower</text>
              </view>
              <view class="modal-fee-amount">
                  <text class="modal-fee-num">0.003049</text>
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          </touchable-opacity>
          <touchable-opacity :on-press="()=>selectType('custom')">
          <view :class="{'modal-fee-container' : selectState !== 'custom', 'selected' : selectState === 'custom'}" :style="{height: windowHeight*0.109}">
              <view class="modal-fee-type">
                  <text class="modal-fee-radio">btn</text>
                  <text class="modal-fee-type-text">Custom</text>
              </view>
              <view class="modal-fee-amount">
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          </touchable-opacity>
          <view :style="{marginTop: '1.7%', marginBottom: '2.8%'}">
          <divider></divider>
          </view>
          <view class='modal-fee-total'>
              <text>Total</text>
              <view class="modal-fee-amount">
                  <text class="modal-fee-num">0.003049</text>
                  <text class="modal-fee-unit">ETH</text>
              </view>
          </view>
          <touchable-opacity>
          <button title="Next"></button>
          </touchable-opacity>
        </view> 
    </view>
    </Modal>
</template>

<script>
import {
  Dimensions,
  Modal
} from "react-native";
import CloseIcon from "../../assets/icon-close.png";
import Divider from "@/components/Divider"
import ButtonMain from "@/components/ButtonMain"

export default {
    data() {
        return{
           CloseIcon,
           selectState : ""
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
        "button": ButtonMain
    },
    methods: {
        selectType(args) {
            console.log("func : selectType")
            this.selectState = args
            console.log(this.selectState)

        },
        close() {
            this.modalVisible = false
            this.$emit('propFromChild', this.modalVisible)
        },
        sendPropToParent() {
            this.$emit('propFromChild', this.modalVisible)
        },
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
}
.modal-fee-radio {
    margin-right: 3.1%;
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
</style>
