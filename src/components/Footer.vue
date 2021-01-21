<template>
  <view class="footer-container" :style="{ backgroundColor: color1 }">
    <text class="footer-text" :style="{ padding: 0, color: color2 }"
      >© 2021 Onther</text
    >
    <view v-if="signedIn" class="footer-row">
      <text class="footer-text" :style="{ color: color2 }">Terms</text>
      <text class="footer-text" :style="{ color: color2 }">Privacy Policy</text>
      <text class="footer-text" :style="{ color: color2 }" :on-press="openURL"
        >About Us</text
      >
    </view>
  </view>
</template>
<script>
import { Linking } from "react-native";
export default {
  methods: {
    openURL() {
      Linking.openURL("https://tokamak.network/");
    },
  },
  props: {
    color1: {
      type: String,
    },
    color2: {
      type: String,
    },
    signedIn: {
      type: Boolean,
    },
  },
};
</script>
<style scoped>
.footer-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  align-items: center;
}
.footer-row {
  display: flex;
  flex-direction: row;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
}

.footer-text {
  font-size: 11px;
  padding: 5px;
  margin-bottom: 10px;
}
</style>