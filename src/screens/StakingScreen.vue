<template>
    <view class="staking-layout">
   <header :navigation="navigation" :back="false"/>
           <view class="staking-container">
         <text>Staking</text>
      </view>
    </view>
</template>
<script>
import Header from '@/components/Header'
export default {
    components: {
      'header': Header,
    },
    props: {
    navigation: {
      type: Object
    }
  },
}
</script>
<style scoped>
.staking-layout{ 
    display: flex;
  flex-direction: column;
  height: 100%;
}
.staking-container {
 flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding-top: 40;
}
</style>