<template>
    <view>
      <header/>
      <text>{{user}}</text>
      <view class="home-layout">
         <balance-component title="Your TON Balance" balance="3.333" rewards="Power TON Balance" value="44.444"/>
         <balance-component/>

      </view>
       
    </view>
</template>
<script>
import { mapState } from 'vuex';
import {store} from '@/store/index';
import Header from '@/components/Header';
import BalanceComponent from '@/components/BalanceComponent';
import Vue from 'vue-native-core';

export default {
  computed: {
    ...mapState([
      'user',
    ]),
  },
    components: {
      'header': Header,
        'balance-component':BalanceComponent,
    },
}
</script>
<style scoped>
.home-layout {
 flex: 1;
  align-self: stretch;
  position: relative;
  display: flex;
  align-items: center;
  padding-top: 40;
}
</style>