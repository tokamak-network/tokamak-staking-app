<template>
  <view class="OperatorInfoSub">
      <text class="OperatorInfoSubTitle">{{title}}</text>
      <text class="OperatorInfoSubContent" 
      :class="{link: title === 'Website', underline: title === 'Operator Address' || title === 'Operator Contract'}"
      :on-press="title === 'Website' || title === 'Operator Address' || title === 'Operator Contract' ? openURL : null"
      >
      {{content}}</text>
  </view>
</template>

<script>
import { Linking } from 'react-native';
import { getConfig } from "../../config.js"

export default {
    data() {
        return{
               uri : ''
        }
    },
    props: ['title', 'content', 'seenWeb'],
    methods: {
        openURL() {
            this.seenWeb = true
            if(this.title === 'Website') {
                this.uri = 'https://tokamak.network/';
            } else {
                this.uri = getConfig().prefixAddress + this.content;
            }
            this.$emit("propFromChild", this.seenWeb, this.uri)
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