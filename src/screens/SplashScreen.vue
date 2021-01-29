<template>
  <view class="splash-layout">
      <image class="splash-logo" :source="require('../../assets/logo-splash.png')" />
      <view v-if="isLoading">
        <activity-indicator class="splash-indicator" size="large" color="#FFFFFF" />
      </view>
      <view v-if="!isLoading">
        <touchable-opacity :on-press="()=>onPress()">
          <text :class="{'splash-wallet-text': pressed === false, 'splash-wallet-text-selected': pressed === true}">Connect Wallet</text>
        </touchable-opacity> 
      </view>
      <text class="splash-footer">© 2021 Onther</text>
  </view>
</template>

<script>
export default {
    data() {
        return{
            isLoading: false,
            pressed: false
        }
    },
    methods: {
        onPress() {
            this.pressed = !this.pressed
        }
    }
}
</script>

<style>
.splash-layout {
    background-color: #2A72E5;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
.splash-logo {
    width: 235px;
    height: 82px;
    align-self: center;
    margin-bottom: 53px;
}
.splash-wallet-text {
    width: 130px;
    height: 35px;
    padding: 7px 18px 9px;
    border-width: 1px;
    border-radius: 19px;
    border-color: #6c9ed0;
    color: #FFFFFF;
    text-align: center;
}
.splash-wallet-text-selected {
    width: 130px;
    height: 35px;
    padding: 7px 18px 9px;
    border-width: 1px;
    border-radius: 19px;
    border-color: #6c9ed0;
    color: #2a72e5;
    text-align: center;
    background-color: #FFFFFF;
}
.splash-footer {
    position: absolute;
    bottom: 20;
    align-self: center;
    color: #FFFFFF;
    font-size: 13px;
    font-weight: 500;
    opacity: 0.5;
}
</style>