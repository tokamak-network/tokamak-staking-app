<template>
  <web-view class="web-container" :source="{uri: url}" />
</template>

<script>
import { WebView } from 'react-native-webview';
export default {
  components: {
        "web-view": WebView
  },
  props: {
    url: {
      type: String,
      default: ''
    }
  }
}
</script>

<style>
.web-container {
  width: 100%;
  height: 100%;
}
</style>