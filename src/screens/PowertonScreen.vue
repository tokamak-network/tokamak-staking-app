<template>
    <view class="power-layout">
   <header :navigation="navigation" :back="false"/>
           <view class="power-container">
         <text class="page-title">Win Power by staking TON!!</text>
      <text class="page-text"
        >Increase your chance of winning Power by staking more TON.</text>
        <winner-table/>
      </view>
    </view>
</template>
<script>
import Header from '@/components/Header';
import WinnerTable from '@/components/WinnerTable';
export default {
    components: {
      'header': Header,
      'winner-table': WinnerTable
    },
    props: {
    navigation: {
      type: Object
    }
  },
}
</script>
<style scoped>
.power-layout{ 
    display: flex;
  flex-direction: column;
  height: 100%;
}
.power-container {
 flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding-top: 20px;
}
.page-title {
  font-size: 30px;
  text-align: center;
  color: #555555;
  padding: 0px 40px;
  font-weight: 700;
}
.page-text {
  font-size: 20px;
  text-align: center;
  padding: 20px 40px;
}
</style>