<template>
  <view class="custom-wrap" :style="{height: windowHeight, width: windowWidth, top: windowHeight * 0.270}">
      <view class="custom-top">
          <touchable-opacity :on-press="()=>backToSelectFee()">
          <image :source=BackIcon :style="{width: windowWidth*0.067, height: windowHeight*0.038}" ></image>
          </touchable-opacity>
          <text class="custom-top-text">Custom</text>
      </view>
      <view class="custom-description">
          <text class="custom-description-text">You can set the free you want to pay, However, if you 
            set it lower than recommended, the transaction might fail, and you’ll still pay the fee.
          </text>
      </view>
      <view class="custom-gas">
          <view class="custom-gas-price">
              <text class="custom-gas-title">Gas price</text>
              <text class="custom-gas-text">= 0.00000018 ETH</text>
          </view>
          <text class="custom-gas-detail">(1 Gwei = 0.000000001 ETH)</text>
          <view class="custom-gas-input-container" :style="{height: windowHeight*0.062, marginBottom: '7.6%'}">
            <text-input
            class="custom-gas-input-field"
            placeholder="0.00"
            autocomplete="off"
            :minLength="1"
            :maxLength="50"
            keyboardType="numeric"
          />
              <text class="custom-gas-input-text">Gwei</text>
          </view>
          <view class="custom-gas-price" :style="{marginBottom: '3%'}">
              <text class="custom-gas-title">Gas limit</text>
          </view>
          <view class="custom-gas-input-container" :style="{height: windowHeight*0.062, marginBottom: '4.5%'}">
            <text-input
            class="custom-gas-input-field"
            placeholder="0.00"
            autocomplete="off"
            :minLength="1"
            :maxLength="50"
            keyboardType="numeric"
          />
          </view>
          <divider :style="{marginBottom: '4.5%'}"></divider>
          <view class="custom-total">
              <text class="custom-total-text">Fee</text>
              <text class="custom-total-text">180 Gwei x 21,000</text>
          </view>
          <view class="custom-total-cryto">
              <text class="custom-total-cryto-text">0.00378 ETH</text>
          </view>
          <view class="custom-total-btn-container">
          <touchable-opacity class="custom-total-btn-wrap btn-cancel" :style="{width: windowWidth*0.417, height: windowHeight*0.062}">
            <text class="btn-cancel-text">Cancel</text>
          </touchable-opacity>
          <touchable-opacity class="custom-total-btn-wrap btn-save" :style="{width: windowWidth*0.417, height: windowHeight*0.062}">
            <text class="btn-save-text">Save</text>
          </touchable-opacity>

          </view>
      </view>
  </view>
</template>

<script>
import {
  Dimensions
} from "react-native";
import BackIcon from "../../assets/back.png"
import Divider from "@/components/Divider"
import ButtonMain from "@/components/ButtonMain"

export default {
    data() {
        return{
            BackIcon,
        }
    },
    components: {
        "divider": Divider,
        "button": ButtonMain
    },
    props: {
        activeTab: {
            type: String
        },
        selectState: {
            type: String
        }
    },
    computed: {
            windowWidth() {
                return Dimensions.get("window").width;
            },
            windowHeight() {
            return Dimensions.get("window").height;
            },
        },
        methods: {
            backToSelectFee() {
                this.activeTab = 'selectFee'
                this.selectState = ''
                this.$emit('propFromChild', this.activeTab, this.selectState)
            }
        }
}
</script>

<style>
.custom-wrap {
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
.custom-top {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 3.9%;
}
.custom-top-text {
    font-size: 16px;
    color: #131315;
    font-weight: bold;
    margin-left: 2.8%;
}
.custom-description {
    margin-bottom: 4.4%;
}
.custom-description-text {
    font-size: 13px;
    color: #86929d;
}
.custom-gas {
    display: flex;
    flex-direction: column;
}
.custom-gas-price {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}
.custom-gas-title {
    font-size: 13px;
    color: #2a72e5;
    font-weight: 700;
}
.custom-gas-text {
    font-size: 13px;
    color: #3e495c;
}
.custom-gas-detail {
    font-size: 12px;
    color: #818992;
    margin-bottom: 3%;
}
.custom-gas-input-container {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-end;
    border-width: 1px;
    border-color: #dfe4ee;
    border-radius: 4px;
    padding-right: 3.1%;
}
.custom-gas-input-field {
    font-size: 15px;
    color: #3e495c;
    margin-right: 2.2%
}
.custom-gas-input-field::placeholder {
    color: pink
}
.custom-gas-input-text {
    font-size: 15px;
    color: #3e495c;
}
.custom-total {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 3%;
}
.custom-total-text {
    font-size: 16px;
    color: #3e495c;
}
.custom-total-cryto {
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    margin-bottom: 14.8%;
}
.custom-total-cryto-text {
    font-size: 16px;
    color: #2a75e5;
}
.custom-total-btn-container {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}
.custom-total-btn-wrap {
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
}
.btn-cancel {
    border-width: 1px;
    background-color: #FFFFFF;
    border-color: #dfe4ee;
}
.btn-cancel-text {
    font-size: 13px;
    color: #3e495c;
}
.btn-save {
    background-color: #257eee;
}
.btn-save-text {
    font-size: 14px;
    color: #FFFFFF;
    font-weight: 700;
}
</style>