import Vue from 'vue'

import wrap from '@vue/web-component-wrapper';
import EquoToolitem from './components/Toolitem';
import EquoToolbar from './components/Toolbar'

const WrappedElement = wrap(Vue, EquoToolitem);
const WrappedElement2 = wrap(Vue,EquoToolbar);

window.customElements.define('equo-toolitem', WrappedElement);
window.customElements.define('equo-toolbar',WrappedElement2);
