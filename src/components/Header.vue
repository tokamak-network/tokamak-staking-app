<template>
    <view>
         <view class="logo-container" >
        <image class="logo" :style="{width: 325, height: 48}" :source="require('../../assets/tokamak-staking-simple.png')" />
      </view>
    </view>
</template>
<script>
export default {
    
}
</script>
<style scoped>
.logo-container {
  flex-direction: row;
  align-items: center;
  justify-content: center;

}
.logo {
  display: flex;
}
</style>