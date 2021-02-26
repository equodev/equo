# JS Toolbar API installation project

## Pre-requirements:
You should have already installed npm/yarn.

Npm version must be 6.0.0 or above.

## Step-by-Step Installation


1.  Execute the following command on Terminal:

```
npm login --scope=@equo --registry https://registry.dev.equoplatform.com
Username: AskForAdminCredentials
Password: AskForAdminCredentials
Email: (this IS public) AskForAdminCredentials
```

2. Open a Terminal on project folder and insert the following command:
   
```
yarn add @equo/toolbar-api 
``` 
or 
```
npm install @equo/toolbar-api
```

3. On JS script, import Toolbar:

```
import { Toolbar } from '@equo/toolbar-api'
```


## How to use the Toolbar Equo API
 You can use several methods to customize the Equo Toolbar using Toolbar var.

**Contruction methods:**

*Toolbar.create()* :It allows to start the customization on our toolbar ( mandatory )

*.build()* : It allows to add to the HTML body with all the customization previously added.

**Behavioral methods:**

*.withBackgroundColor(String)*: It allows to change the toolbar's background color using rgb or hexacode.

*.addToolItem()*: It allows to add a new item to the toolbar.


Once we add a item, we can specify the item, tooltip and onClick event function that it will have, using the following methods:

*.addTooltip(String)*: It defines a toolitem's description

*.addIcon(String)*: It allows to add an specific icon on the new toolItem. The available icons to this toolbar belong to the fontawesome library. Here, in [Fontawesome Icons](https://fontawesome.com/icons?d=gallery&s=regular,solid&m=free) website, we can choose the desired icon just adding the icon name as method's parameter.

*.onClick(Function)*: It allows to add functionality when the item is clicked. 

