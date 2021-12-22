import { useEffect, useState } from "react";
import './App.css';
import { EquoCommService } from "@equo/comm";
const comm = EquoCommService.get();

function App() {
  const [message, setMessage] = useState("")

  useEffect(() => {
    comm.on("MyJavaEvent", (res) => {
      console.log(res);
      setMessage(res)
    });
  }, [])

  const clickEvent = () => {
    comm.send("MyEventHandler", "Hi, I am a message from the frontend!");
  }

  return (
    <div class="container">
        <img
          alt="logo equo"
          class="logo"
          src="https://sfo2.digitaloceanspaces.com/equo-cms/2020/08/28/5f495e535cda5Isotipo.png"
        />
        <h1>Welcome to your first Equo App</h1>
        <p>For a guide and recipes on how to configure / customize an Equo App,
            check out the <a href="https://docs.equoplatform.com/" target="_blank" rel="noreferrer noredirect"> SDK documentation</a> .</p>
        <h2>Useful Links</h2>
        <div class="useful-links">
            <div class="useful-minicard">
                <a class="link" target="_blank" rel="noreferrer noredirect" href="https://docs.equoplatform.com/sdk/1.1.x/getting-started/creating.html">
                    Development guide
                    <img
                      alt="arrow icon"
                      class="arrow-icon"
                      src="arrow-icon.png"
                    />
                </a>
            </div>
            <div class="useful-minicard">
                <a class="link" target="_blank" rel="noreferrer noredirect" href="https://docs.equoplatform.com/sdk/1.1.x/developing/hot-reload.html">
                    Web UI frameworks Development
                    <img 
                      alt="arrow icon"
                      class="arrow-icon"
                      src="arrow-icon.png" 
                    />
                </a>
            </div>
        </div>
        <div class="useful-links">
            <div class="useful-minicard">
                <a class="link" target="_blank" rel="noreferrer noredirect" href="https://docs.equoplatform.com/sdk/1.1.x/index-api-javascript.html">
                    Javascript API documentation
                    <img
                      alt="arrow icon"
                      class="arrow-icon"
                      src="arrow-icon.png"
                    />
                </a>
            </div>
            <div class="useful-minicard">
                <a class="link" target="_blank" rel="noreferrer noredirect" href="https://docs.equoplatform.com/sdk/1.1.x/index-api-java.html">
                    Java API documentation
                    <img
                      alt="arrow icon"
                      class="arrow-icon"
                      src="arrow-icon.png"
                    />
                </a>
            </div>
        </div>
        <div class="useful-links">
          <div class="useful-minicard">
            <div
              class="link"
              onClick={clickEvent}
            >
              Click me!
            </div>
          { message }
          </div>
        </div>

        <h2>Ecosystem</h2>
        <p>Learn Equo <a href="https://equoplatform.com" target="_blank" rel="noreferrer noredirect">here!</a> </p>
    </div>
  );
}

export default App;
