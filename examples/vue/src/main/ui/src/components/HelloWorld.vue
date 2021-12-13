<template>
  <div class="container">
    <img
      class="logo"
      src="https://sfo2.digitaloceanspaces.com/equo-cms/2020/08/28/5f495e535cda5Isotipo.png"
    />
    <h1>Welcome to your first Equo App</h1>
    <p>
      For a guide and recipes on how to configure / customize an Equo App, check
      out the
      <a
        href="https://docs.equoplatform.com/"
        target="_blank"
        rel="noreferrer noredirect"
      >
        Framework documentation
      </a> .
    </p>
    <h2>Useful Links</h2>
    <div class="useful-links">
      <div class="useful-minicard">
        <a
          class="link"
          href="https://docs.equoplatform.com/framework/1.1.x/developing/first-application.html"
          target="_blank"
          rel="noreferrer noredirect"
        >
          Development guide
          <img class="arrow-icon" src="../assets/arrow-icon.png" />
        </a>
      </div>
      <div class="useful-minicard">
        <a
          class="link"
          href="https://docs.equoplatform.com/framework/1.1.x/developing/hot-reload.html"
          target="_blank"
          rel="noreferrer noredirect"
        >
          Web UI frameworks Development
          <img class="arrow-icon" src="../assets/arrow-icon.png" />
        </a>
      </div>
    </div>
    <div class="useful-links">
      <div class="useful-minicard">
        <a
          class="link"
          href="https://docs.equoplatform.com/framework/1.1.x/index-api-javascript.html"
          target="_blank"
          rel="noreferrer noredirect"
        >
          Javascript API documentation
          <img class="arrow-icon" src="../assets/arrow-icon.png" />
        </a>
      </div>
      <div class="useful-minicard">
        <a
          class="link"
          href="https://docs.equoplatform.com/framework/1.1.x/index-api-java.html"
          target="_blank"
          rel="noreferrer noredirect"
        >
          Java API documentation
          <img class="arrow-icon" src="../assets/arrow-icon.png" />
        </a>
      </div>
    </div>
    <div class="useful-links">
      <div class="useful-minicard">
        <div
          class="link"
          @click="clickEvent"
        >
          Click me!
        </div>
      {{ message }}
      </div>
    </div>

    <h2>Ecosystem</h2>
    <p>Learn Equo <a href="https://equoplatform.com" target="_blank" rel="noreferrer noredirect">here!</a></p>
  </div>
</template>

<script>
import { EquoCommService } from "@equo/comm";
const comm = EquoCommService.get();

export default {
  name: "HelloWorld",
  data() {
    return {
      message: "",
    };
  },
  mounted() {
    comm.on("MyJavaEvent", (res) => {
      console.log(res);
      this.message = res;
    });
  },
  methods: {
    clickEvent() {
      comm.send("MyEventHandler", "Hi, I am a message from the frontend!");
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;

  padding-top: 0px;
}

.logo {
  height: 300px;
  width: 300px;
}

.useful-links {
  align-items: center;
  display: flex;
  flex-direction: row;
}

.useful-minicard {
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;

  border-radius: 10px;
  border: 1px rgba(150, 121, 255, 0.377) solid;
  height: 80px;
  width: 350px;

  padding: 25px;
  margin: 15px;
}

.useful-minicard:hover {
  background-color: #3884ff;
}

.link {
  align-items: center;
  color: black;
  display: flex;
  justify-content: center;

  text-decoration: none;
}

.arrow-icon {
  height: 30px;

  margin-left: 10px;
}
</style>
