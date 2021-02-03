<template>
  <view class="OperatorInfoSub">
      <text class="OperatorInfoSubTitle">{{title}}</text>
      <text class="OperatorInfoSubContent" 
      :class="{link: title === 'Website', underline: title === 'OperatorAddress' || title === 'OperatorContract'}"
      :on-press="title === 'Website' || title === 'OperatorAddress' || title === 'OperatorContract' ? openURL : null"
      >
      {{content}}</text>
  </view>
</template>

<script>
import { Linking } from 'react-native';
export default {
    props: {
        title: {
            type: String,
            default: ''
        },
         content: {
            type: String,
            default: ''
        },
    },
    methods: {
      openURL () {
          Linking.openURL('https://tokamak.network/')
      }
  } 
}
</script>

<style>
.OperatorInfoSub {
    display: flex;
    flex-direction: column;
    margin-bottom: 7.2%;
}
.OperatorInfoSubTitle {
    font-size: 13px;
}
.OperatorInfoSubContent {
    font-size: 12px;
}
.link {
    color: #2a72e5;
    font-weight: bold;
}
.underline {
text-decoration-line: underline;
}
</style>