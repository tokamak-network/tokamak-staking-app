<template>
   <view  class="login-container">
        <text-input
        :style="{height: 40, width: 100, borderColor: 'gray', borderWidth: 1}"
        v-model="text"
      />
        <button :on-press="login" title="Login" />
    </view>
</template>

<script>
import { NativeModules, Alert } from 'react-native';
import { mapState } from 'vuex';
const { BlockchainModule } = NativeModules;

export default {
    props: {
    navigation: {
      type: Object
    },
    text: '',
    // store: {
    //     type: Object
    // }
  },
    methods: {
        login () {

            this.$store.dispatch('login')
            this.navigation.navigate('Home');
        }
    }
}
</script>
<style scoped>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    flex: 1;
}
</style>