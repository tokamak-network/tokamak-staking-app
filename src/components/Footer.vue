<template>
    <view class="footer-container">
         <text class="footer-text" :style="{padding: 0}">© 2021 Onther</text>
         <view class="footer-row">
          <text class="footer-text">Terms</text>
          <text class="footer-text">Privacy Policy</text>
          <text class="footer-text" :on-press="openURL">About Us</text>
         </view>
    </view>
</template>
<script>
import { Linking } from 'react-native';
export default {
  methods: {
      openURL () {
          Linking.openURL('https://tokamak.network/')
      }
  } 
}
</script>
<style scoped>
.footer-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    align-items: center;
}
.footer-row {
    display: flex;
    flex-direction: row;
   flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.footer-text {
    font-size: 11px;
     color: #2a72e5;
     padding: 10px 20px;
}
</style>