<template>
  <HelloWorld />
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',
  components: {
    HelloWorld
  }
}
</script>

<style>
body {
    font-family: 'Rajdhani', sans-serif;
}

h1 {
    font-size: 3.75rem;
    margin-bottom: 0px;
    margin-top: 0px;
}

p {
    color: #62676c;
    font-size: 1.2rem;
}

a {
    color: #3884ff;
    font-size: 1.2rem;
}
</style>
