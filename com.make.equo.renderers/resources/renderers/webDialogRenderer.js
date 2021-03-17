$(document).ready(function () {

    let dialog = {
        template: `
        <div class="el-dialog__wrapper" @click.self="handleWrapperClick">
        <div
          aria-modal="true"
          :aria-label="title || 'dialog'"
          ref="dialog">
          <div class="el-dialog__header">
            <div name="title">
              <i v-if="dialogType === '1'" class="el-icon-error"></i>
              <i v-else-if="dialogType === '2'" class="el-icon-info"></i>
              <i v-else-if="dialogType === '3' || dialogType === '5' || dialogType === '6'" class="el-icon-question"></i>
              <i v-else-if="dialogType === '4'" class="el-icon-warning"></i>
              <span class="el-dialog__title">{{ title }}</span>
            </div>
            <button
              type="button"
              class="el-dialog__headerbtn"
              aria-label="Close"
              @click="handleClose">
              <i class="el-dialog__close el-icon el-icon-close"></i>
            </button>
          </div>
          <div class="el-dialog__body">
            <span> {{ message }} </span>
          </div>
          <div>
              <template v-if= "isToggle === true">
                  <input type="checkbox" id="checkbox" v-model="toggle.status">
                  <label for="checkbox">{{ toggle.message }}</label>
              </template>
          </div>
          <div class="el-dialog__footer">
            <template v-for="button in buttons">
                <template v-if="button.bLabel.startsWith('&')">
                    <el-button type="primary" @click="callE4Command(button.action)"> {{ button.bLabel.replace('&', '') }} </el-button>
                </template>
                <template v-else>
                    <el-button @click="callE4Command(button.action)"> {{ button.bLabel.replace('&', '') }} </el-button>
                </template>
            </template>
          </div>
        </div>
      </div>
      `,
        data() {
            return {
            };
        },
        props: {
            namespace: {
                type: String,
                required: true
            },
            isToggle: {
                type: Boolean,
                required: true
            },
            buttons: {
                type: Array,
                required: true
            },
            title: {
                type: String,
                required: true
            },
            message: {
                type: String,
                required: true
            },
            dialogType: {
                type: String,
                required: true
            },
            toggle: {
              type: Object
            }
        },
        methods: {
            callE4Command(commandId) {
                equo.send(this.namespace + '_itemClicked', {
                    command: commandId
                });
            },
            handleWrapperClick() {
                if (!this.closeOnClickModal) {
                    return;
                }
                this.handleClose();
            },
            handleClose() {
                this.callE4Command("-1");
            }
        }
    };
    Vue.component("dialog-comp", dialog);

    // Add dialog to the html body
    const createDialog = function (namespace, e4Model) {
        let title = e4Model[0].title;
        let message = e4Model[0].message;
        let dialogType = e4Model[0].type;
        let isToggle = (e4Model[0].isToggle == 'true');
        let toggle = {};
        let buttons = [];

        if(isToggle === true){
          toggle = e4Model[1];
          buttons = e4Model.splice(2);
        }else{
          buttons = e4Model.splice(1);
        }

        let webDialog = Vue.extend(dialog);

        let component = new webDialog({
            propsData: {
                namespace,
                isToggle,
                buttons,
                title,
                message,
                dialogType,
                toggle
            }
        }).$mount()

        $('body').append(component.$el)
    }

    equo.getE4Model(createDialog);
});