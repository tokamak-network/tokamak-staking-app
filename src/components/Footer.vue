<template>
    <view class="footer-container">
         <text class="footer-text" :style="{padding: 0}">© 2021 Onther</text>
         <view class="footer-row">
          <text class="footer-text">Terms</text>
          <text class="footer-text">Privacy Policy</text>
          <text class="footer-text">About Us</text>
         </view>
    </view>
</template>
<script>

export default {
   
}
</script>
<style scoped>
.footer-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    /* justify-content: flex-end; */
    /* flex: 1; */
    /* align-self: stretch; */
    /* position: relative; */
    align-items: center;
    /* height: 40px; */
}
.footer-row {
    display: flex;
    flex-direction: row;
   flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.footer-text {
    font-size: 15px;
     color: #2a72e5;
     padding: 10px 20px;
}
</style>