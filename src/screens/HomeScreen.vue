<template>
    <view class="home-layout">
      <header :openDrawer="openDr"/>
      <view class="home-container">
         <balance-component title="Your TON Balance" balance="3.333 TON" rewards="Power TON Balance" value="0 POWER"/>
         <balance-component title="Total Staked Amount" balance="6.77 TON" rewards="Total Staked Amount" value="10.05 TON"/>
      </view>
        <footer/>
    
    </view>
</template>
<script>
import { mapState } from 'vuex';
import {store} from '@/store/index';
import Header from '@/components/Header';
import BalanceComponent from '@/components/BalanceComponent';
import Vue from 'vue-native-core';
import Footer from '@/components/Footer'

export default {
  computed: {
    ...mapState([
      'user',
    ]),
  },
    components: {
      'header': Header,
        'balance-component':BalanceComponent,
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
.home-layout {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.home-container {
 flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding-top: 40;
}
</style>