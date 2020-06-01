const app = new Vue({
	el: "#title",
	data: {
		title: "Equo plain App",
		nodes: [
		    {title: 'Item1', isLeaf: true},
		    {title: 'Item2', isLeaf: true, data: { visible: false }},
		    {title: 'Folder1'},
		    {
		      title: 'Folder2', isExpanded: true, children: [
		        {title: 'Item3', isLeaf: true},
		        {title: 'Item4', isLeaf: true}
		      ]
		    }
		  ]
	}
});

const tree = new Vue({
	el: "#tree",
	components: {SlVueTree},
	data: {
		title: "Equo plain App",
		nodes: [
		    {title: 'Item1', isLeaf: true},
		    {title: 'Item2', isLeaf: true, data: { visible: false }},
		    {title: 'Folder1'},
		    {
		      title: 'Folder2', isExpanded: true, children: [
		        {title: 'Item3', isLeaf: true},
		        {title: 'Item4', isLeaf: true}
		      ]
		    }
		  ]
	}
});
