$(document).ready(function () {

    let dialog = {
        template: `
        <div class="el-dialog__wrapper" @click.self="handleWrapperClick">
        <div
          aria-modal="true"
          :aria-label="title || 'dialog'"
          ref="dialog">
          <div class="el-dialog__header">
            <slot name="title">
              <span class="el-dialog__title">{{ title }}</span>
            </slot>
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
          <div class="el-dialog__footer">
            <template v-for="button in buttons">
                <template v-if="button.bLabel === 'Confirm' || button.bLabel === 'Yes'">
                    <el-button type="primary" @click="callE4Command(button.action)"> {{ button.bLabel }} </el-button>
                </template>
                <template v-else>
                    <el-button @click="callE4Command(button.action)"> {{ button.bLabel }} </el-button>
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
        let buttons = e4Model.splice(1);

        let webDialog = Vue.extend(dialog);

        let component = new webDialog({
            propsData: {
                namespace,
                buttons,
                title,
                message,
            }
        }).$mount()

        $('body').append(component.$el)
    }

    equo.getE4Model(createDialog);
});