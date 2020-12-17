<template>
    <view class="staking-layout">
      <header :openDrawer="openDr"/>
        <view class="staking-container">
         <text>Staking</text>
      </view>
        <footer/>
    </view>
</template>
<script>
import Header from '@/components/Header'
import Footer from '@/components/Footer'
export default {
    components: {
      'header': Header,
      'footer': Footer,
    },
    props: {
    navigation: {
      type: Object
    }
  },
    methods: {
      openDr (){
         this.navigation.openDrawer();
      }
    }
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