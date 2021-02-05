<template>
  <view class="account-wrap">
    <view class="account-top">
      <text class="account-top-title">My Account</text>
      <text class="account-top-content"
        >You can check your account and balance.</text
      >
    </view>
    <view
      class="account-address"
      :style="{ marginBottom: windowHeight * 0.033 }"
    >
      <text class="account-address-title">Account Address</text>
      <text class="account-address-content">{{user| hexSlicer}}</text>
    </view>
    <view class="account-wallet">
      <view
        class="account-wallet-container"
        :style="{
          height: windowHeight * 0.090,
          marginBottom: windowHeight * 0.023,
        }"
      >
        <text class="account-wallet-container-text">ETH Balance</text>
        <text class="account-wallet-container-text-amount">{{currencyAmount(ethBalance).substring(0, currencyAmount(ethBalance).length - 3)}}</text>
        <text class="account-wallet-container-text-unit">ETH</text>
      </view>
      <view
        class="account-wallet-container"
        :style="{
          height: windowHeight * 0.090,
          marginBottom: windowHeight * 0.023,
        }"
      >
        <text class="account-wallet-container-text">TON Balance</text>
        <text class="account-wallet-container-text-amount">{{currencyAmount(tonBalance).substring(0, currencyAmount(tonBalance).length - 3)}}</text>
        <text class="account-wallet-container-text-unit">TON</text>
      </view>
      <view
        class="account-wallet-container"
        :style="{
          height: windowHeight * 0.090,
          marginBottom: windowHeight * 0.023,
        }"
      >
        <text class="account-wallet-container-text">POWER Balance</text>
        <text class="account-wallet-container-text-amount">{{currencyAmount(power).substring(0, currencyAmount(power).length - 5)}}</text>
        <text class="account-wallet-container-text-unit">POWER</text>
      </view>
    </view>
   <touchable-opacity
            :on-press="() => logout()"
            class="account-btn"
          >
    <view >
      <text class="account-btn-text">Logout</text>
    </view>
   </touchable-opacity>
  </view>
</template>

<script>
import { Dimensions } from "react-native";
import { mapState } from "vuex";

export default {
  computed: {
    ...mapState(["ethBalance", "tonBalance", "power", "user"]),
    currencyAmount() {
      return (amount) => this.$options.filters.currencyAmount(amount);
    },
    windowWidth() {
      return Dimensions.get("window").width;
    },
    windowHeight() {
      return Dimensions.get("window").height;
    },
  },
  methods: {
      logout () {
          this.$store.dispatch('logout')
      }
  }
};
</script>

<style>
.account-wrap {
  display: flex;
  flex-direction: column;
  padding-left: 5.6%;
  padding-right: 5.6%;
  background-color: #fafbfc;
  height: 100%;
}
.account-top {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding-top: 5.6%;
  margin-bottom: 10%;
}
.account-top-title {
  font-size: 24px;
  color: #3e495c;
  font-weight: bold;
  margin-bottom: 0.8%;
}
.account-top-content {
  font-size: 12px;
  color: #86929d;
}
.account-address {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding-left: 3.7%;
  padding-right: 3.7%;
}
.account-address-title {
  font-size: 13px;
  color: #3e495c;
  font-weight: bold;
}
.account-address-content {
  font-size: 13px;
  color: #2a72e5;
  font-weight: bold;
}
.account-wallet {
  display: flex;
  flex-direction: column;
  margin-bottom: 4.7%;
}
.account-wallet-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  border-width: 1px;
  border-radius: 10px;
  border-color: #e7ebf2;
  background-color: #ffffff;
  padding-left: 4.7%;
  padding-right: 4.7%;
}
.account-wallet-container-text {
  font-size: 15px;
  color: #86929d;
}
.account-wallet-container-text-amount {
  font-size: 18px;
  color: #131315;
  font-weight: bold;
}
.account-wallet-container-text-unit {
  position: absolute;
  right: 7%;
  bottom: 14.3%;
  font-size: 10px;
  color: #86929d;
}
.account-btn {
  height: 6.5%;
  border-width: 1px;
  border-color: #2a72e5;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ffffff;
}
.account-btn-text {
  font-size: 13px;
  color: #2a72e5;
  font-weight: bold;
}
</style>
