<template>
  <view class="staking-layout">
    <header :navigation="navigation" :back="true" />
    <view class="staking-container">
      <view class="avatar" :style="{ backgroundColor: color }">
        <text class="opr">O P R</text>
      </view>
      <text class="title">{{ name }}</text>
      <view class="info-row">
        <text class="info-title">Commission Rate</text>
        <text class="info-description">2.5%</text>
      </view>
      <view class="info-row">
        <text class="info-title">Most Recent Commit</text>
        <text class="info-description">a day ago</text>
      </view>
      <view class="info-row">
        <text class="info-title">My Staked</text>
        <text class="info-description">5664.34 TON</text>
      </view>
      <view class="amount-container">
        <view class="amount-row">
          <text class="info-title">Availablr Amount</text>
          <text class="info-description">4598.24 TON</text>
        </view>
        <view
          class="amount-row"
          :style="{ backgroundColor: '#FFFFFF', padding: 8, borderRadius: 13 }"
        >
          <text class="info-title">Amount</text>
          <view class="input-text">
            <text-input
              class="input"
              v-model="text"
              placeholder="0.00"
              autocomplete="off"
              minlength="1"
              maxlength="1"
              keyboardType="numeric"
            />
            <text class="info-description">TON</text>
          </view>
        </view>
         <touchable-opacity :on-press="onPressButton">
        <view class="button-stake">
          <text class="opr" >Stake</text>
        </view>
         </touchable-opacity>
      </view>
    </view>
  </view>
</template>
<script>
import Header from "@/components/Header";
import { Button} from 'react-native';
export default {
  components: {
    header: Header,
  },
  data() {
    return {
      name: "",
      text: "",
      color: "",
      nav: {
        type: Object,
      },
    };
  },
  props: {
    navigation: {
      type: Object,
    },
    route: {
      type: Object,
    },
  },
  created() {
    this.color = this.navigation.state.params.color;
    this.name = this.navigation.state.params.name;
    this.nav = this.navigation.state.params.navigation;
  },
  methods: {
    onPressButton() {
    },
  },
};
</script>
<style scoped>
.staking-layout {
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
  padding-top: 20;
}
.title {
  color: #8c8c8c;
  font-size: 28px;
  padding-bottom: 20px;
  color: #555555;
  font-weight: 700;
}
.avatar {
  display: flex;
  width: 65px;
  height: 65px;
  font-size: 30px;
  font-weight: 700;
  border-radius: 50;
  justify-content: center;
  align-items: center;
}
.opr {
  color: white;
  font-size: 20px;
  font-weight: 600;
}
.info-row {
  padding-top: 10px;
  padding-left: 30px;
  padding-right: 30px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-self: stretch;
  align-items: center;
  position: relative;
}
.info-title {
  color: #555555;
  font-size: 18px;
}
.info-description {
  color: #555555;
  font-size: 18px;
  font-weight: 700;
}
.amount-container {
  width: 85%;
  background-color: #e2e8eb;
  border-width: 1;
  border-color: #ccd1d3;
  border-radius: 13;
  height: 230px;
  padding: 15px;
  margin: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.amount-row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-bottom: 10px;
  height: 40px;
  align-self: stretch;
  align-items: center;
  position: relative;
  width: 100%;
}
.input-text {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-self: stretch;
  align-items: center;
  position: relative;
}
.input {
  text-align: right;
  height: 20px;
  font-size: 18px;
  width: 20px;
  overflow: hidden;
  width: 80px;
  margin-right: 20px;
  align-items: center;
  color: #555555;
  margin-right: 8px;
}
.button-stake {
  background-color: #2a72e5;
  color: #ffffff;
  border-radius: 13px;
  width: 150px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}
</style>
