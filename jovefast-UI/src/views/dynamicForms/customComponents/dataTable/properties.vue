<template>
<div v-if="selectItem && selectItem.key && selectItem.type == 'dataTable'" class="form-tableList-properties">
	<el-collapse-item title="样式配置" name="config">
		<el-form  size="mini" :disabled="disabled"  label-width="80px"> 
			<el-form-item label="边框">
         <el-switch v-model="options.border"/>
      </el-form-item>
      <el-form-item label="斑马线">
         <el-switch v-model="options.stripe"/>
      </el-form-item> 
      <el-form-item label="下标">
         <el-switch v-model="options.showIndex"/>
      </el-form-item> 
      
		</el-form>
	</el-collapse-item>
	<el-collapse-item title="数据配置" name="dataset">
		<!-- 自定义组件的属性配置 -->  
	<!-- 	<el-form size="mini" :disabled="disabled" label-position="top" label-width="100px"> 
			<el-form-item label="请求参数" >
	      <QeuryList v-model="options.queryData" />
	    </el-form-item>
  	</el-form>  -->
		<el-form size="mini" :disabled="disabled"  label-width="80px"> 
	    <!--   
		 	dbType: 1 , // 1-静态 2-api
			datasourceStatic: [] ,// 静态数据
			methodType: 'get', // API模式下请求方法类型 get post
			apiPath: '' , // API模式下请求地址
			queryData: [] , // API请求下要携带的数据
			apiDataScript: '' , // 请求到的数据转列表数组方法 自定义脚本
			columns: [] ,// 字段配置 格式: {field: '' , label: '' , width: '10%' , align: 'center'}
			page: false , // 是否分页 在分页模式下需要配置每页数据量 和当前页字段key和每页数据量字段key
			currentPageKey: 'pageIndex', // 当前页key
			pageSizeKey: 'pageSize' ,// 也没数据量KEY
			pageSize: 10 // 每页数据量
		-->   
 		<el-form-item label="请求参数" >
	    
	  </el-form-item>
	  <QeuryList v-model="options.queryData" />
	  <el-divider ></el-divider>
          	<el-form-item label="数据来源">
            	<el-radio-group v-model="options.dbType">
              		<el-radio :label="1">静态数据</el-radio>
              		<el-radio :label="2">API</el-radio>  
            	</el-radio-group>
          	</el-form-item>
          	<el-form-item label="静态数据" v-if="options.dbType == 1"> 
            	 <el-button type="primary" @click="configStaticDataHandle"> 配置 </el-button>
          	</el-form-item>
            <el-divider ></el-divider>
            <template v-if="options.dbType == 2">
            	<el-form-item  label="请求方法">
	            	<el-radio-group v-model="options.methodType">
	              		<el-radio-button label="get">GET</el-radio-button>
	              		<el-radio-button label="post">POST</el-radio-button> 
	            	</el-radio-group>
	          	</el-form-item>  
	          	<el-form-item  label="API地址"> 
            		<el-input type="textarea" v-model="options.apiPath" placeholder="API地址URL" ></el-input>
          		</el-form-item> 
          		<el-form-item  label="解析方法"> 
            		<el-input type="textarea" :rows="6" v-model="options.apiDataScript" placeholder="解析方法: 
            		(data)=> {
					if(data && data.code == 200) {
						const list = data.data 
						return {total: list.length , list: list}
					}
					return null
					}" ></el-input>
          		</el-form-item> 
            </template>
          	
          	<el-form-item label="分页"> 
            	<el-switch v-model="options.page" />
          	</el-form-item> 
          	<el-form-item label="每页数据" v-if="options.page"> 
            	<el-input-number  
		            v-model="options.pageSize"
		            controls-position="right"
		            :min="1"
		            :max="100" 
		            :step="1"
		        /> 
          	</el-form-item> 
          	<el-form-item  label="页码KEY" v-if="options.page && options.dbType == 2"> 
            	<el-input   v-model="options.currentPageKey" placeholder="API请求时当前页key" ></el-input>
          	</el-form-item> 
          	<el-form-item  label="数量KEY" v-if="options.page && options.dbType == 2"> 
            	<el-input   v-model="options.pageSizeKey" placeholder="API请求时每页数据量KEY" ></el-input>
          	</el-form-item> 
		</el-form>
	</el-collapse-item>
	 
    <!-- 字段配置 -->   
    <el-collapse-item title="字段列表" name="columns">
        <div :key="datasetKey" class="data-set">
            <el-tree :data="options.columns" class="table-column-tree" default-expand-all :expand-on-click-node="false">
              <span style="width: 100%;" slot-scope="{ node, data }">
                <el-row class="tree-row">
                  <el-col :span="9">
                    <span class="pointer " @click="addOrUpdateColumn(node)">{{data.label}}</span>
                  </el-col>
                  <el-col :span="9">
                    <span class="pointer " @click="addOrUpdateColumn(node)">{{data.column}}</span>
                  </el-col>
                  <el-col :span="6">
                    <el-button type="text" icon=" el-icon-plus" @click="appendColumn(node)"></el-button>
                    <el-button type="text" icon=" el-icon-close" @click="removeColumn( node)"></el-button>
                  </el-col>
                </el-row>
              </span>
            </el-tree>
            <el-button type="text" icon="el-icon-plus" @click="addOrUpdateColumn()"></el-button>
        </div>
    </el-collapse-item> 
	<AddOrUpdateColumn ref="columnset" v-if="columnVisisble"   @add="addDataColumn"  @update="updateDataColumn"/>

	<el-dialog
    title="静态数据"
    :close-on-click-modal="false"
    :modal-append-to-body="false"
    :visible.sync="staticVisible">

    <el-input  type="textarea" :rows="10" v-model="staticText" placeholder="静态数据JSON" ></el-input>

    <span slot="footer" class="dialog-footer" >
      <el-button size="mini" @click="staticVisible = false">取消</el-button>
      <el-button size="mini" type="primary" @click="staticDataSubmit()">确定</el-button>
    </span>
  </el-dialog>

</div>    
</template>
<script>
import AddOrUpdateColumn from './add-or-update-column'
import QeuryList from './query-list.vue'
export default {
	components: {
		AddOrUpdateColumn , QeuryList
	},
	data() {
		return {
			columnVisisble: false,
			staticVisible: false,
			staticText: '' ,
			datasetKey: 1
		}
		
	},
	props: {
		selectItem: { // 当前选择的组件
	      type: Object,
	      required: true
	    },
	    disabled: { // 是否禁用
	      type: Boolean,
	      default: false
	    }
	},
	computed: {
		options() {
			return this.selectItem.options
		}
	},
	methods: {
		configStaticDataHandle(parent) {
			if(this.options.datasourceStatic) {
				this.staticText = JSON.stringify(this.options.datasourceStatic)
			} else {
				this.staticText = '[]'
			}

			this.staticVisible = true
			  
			
		},
		generateId () {
	      return new Date().getTime() + parseInt(Math.random() * 1000000)
	    },
		// 追加字段 
	    appendColumn (node) {
	      this.columnVisisble = true
	      this.$nextTick(() => {
	        this.$refs.columnset.init(null, node.data)
	      })
	    },
	    removeColumn (node) {
	      const parent = node.parent
	      if (!parent) {
	        // 顶层节点 直接从数组中删除
	        let columns = this.options.columns
	        columns = columns.filter((val) => val && val.id != node.data.id)
	        this.$set(this.selectItem, 'columns', columns)
	      } else {
	        const pdata = parent.data
	        // 从parent中删除
	        if (pdata instanceof Array) {
	          let columns = pdata
	          const index = columns.findIndex(d => d.id === node.data.id);
	          columns.splice(index, 1);
	        } else {
	          let columns = pdata.children
	          columns = columns.filter((val) => val && val.id != node.data.id)
	          this.$set(pdata, 'children', columns)
	        }
	      }
	      this.datasetKey++
	    },
	    addDataColumn (data, parent) {
	      // 给一个id 
	      data.id = this.generateId()
	      if (!parent) {
	        // 顶层节点 直接从数组中删除
	        let columns = this.options.columns
	        columns.push(data)
	        this.$set(this.options, 'columns', columns)
	      } else {
	        // 从parent中删除
	        if (parent instanceof Array) {
	          let columns = parent
	          columns.push(data)
	        } else {
	          let columns = parent.children
	          if (columns == undefined || columns == null) {
	            columns = [data]
	          } else {
	            columns.push(data)
	          }
	          this.$set(parent, 'children', columns)
	        }
	      }
	      this.datasetKey++
	    },
	    updateDataColumn (data, parent) {
	      const index = data.index
	      if (!parent) {
	        // 顶层节点 直接从数组中删除  
	        let columns = this.options.columns
	        columns[index] = data
	        this.$set(this.options, 'columns', columns)
	      } else {
	        // 从parent中删除
	        let columns = parent instanceof Array ? parent : parent.children
	        columns[index] = data
	        this.$set(parent, 'children', columns)
	      }
	      this.datasetKey++
	    },
	    addOrUpdateColumn (node) {
	      this.columnVisisble = true
	      let row = null
	      let parentData = null
	      if (node) {
	        row = node.data
	        const parent = node.parent;
	        const children = parent.data.children || parent.data;
	        const index = children.findIndex(d => d.id === node.data.id);
	        row.index = index
	        parentData = parent.data
	      }
	      this.$nextTick(() => {
	        this.$refs.columnset.init(row, parentData)
	      })
	    },
	    staticDataSubmit() {
	    	try {
	        const staticData = JSON.parse(this.staticText)
	        
	        if(staticData && staticData instanceof Array){
	          this.options.datasourceStatic = staticData

	          this.staticVisible = false
	        } else {
	          this.$message.error('数据解析异常，请检查JSON数据内容.')
	          return
	        }
	        
	      } catch (error) {
	        this.$message.error('数据解析异常，请检查JSON数据内容.')
	        return
	      }
	    }

	}
}
</script>
<style>
.form-tableList-properties .data-set{
	padding: 10px;
}

.form-tableList-properties .table-column-tree .el-col{
   
    margin-bottom: 0px;
   
}
.table-column-tree .tree-row {
	display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    height: 26px;
    cursor: pointer;
}

</style>