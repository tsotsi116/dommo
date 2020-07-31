<template>
  <div id="app">
    <img src="./assets/logo.png" />

    <h1>Dommo</h1>

    <RequestVerification productID="" />
  </div>
</template>

<script>
import RequestVerification from './components/RequestVerification.vue'

export default {
  name: 'App',
  components: {
    RequestVerification
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  margin: 0 auto;
  width: 80%;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
