<template>
   <view  class="login-container">
          
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
  created() {
    this.login();
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
  background-color: #2a72e5;
    display: flex;
    justify-content: center;
    align-items: center;
    flex: 1;
}
.button-login {
width: 130;
height: 35;
border-width: 1;
    border-color: #ccd1d3;
    border-radius: 13;
     display: flex;
    justify-content: center;
    align-items: center;
}

.button-name {
  color: #ffffff;
}
</style>