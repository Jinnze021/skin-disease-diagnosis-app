<template>
	<el-main class="bg edit_wrap comtable_e">
		<el-form ref="form" :model="form" status-icon label-width="120px" v-if="is_view()">
		<el-row class="row_ce"> 
							<el-col v-if="$check_field('get','registered_user') || $check_field('add','registered_user') || $check_field('set','registered_user')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="注册用户" prop="registered_user">
																					<div v-if="user_group !== '管理员'">
							{{ get_user_session_registered_user(form['registered_user']) }}
							<el-select v-if="(form['skin_detection_id'] && $check_field('set','registered_user')) || (!form['skin_detection_id'] && $check_field('add','registered_user'))" id="registered_user" v-model="form['registered_user']" :disabled="disabledObj['registered_user_isDisabled']">
								<el-option v-for="o in list_user_registered_user" :key="o['username']" :label="o['nickname'] + '-' + o['username']"
										   :value="o['user_id']">
								</el-option>
							</el-select>
							<el-select v-else-if="$check_field('get','registered_user')" id="registered_user" v-model="form['registered_user']" :disabled="true">
								<el-option v-for="o in list_user_registered_user" :key="o['username']" :label="o['nickname'] + '-' + o['username']"
										   :value="o['user_id']">
								</el-option>
							</el-select>
						</div>
						<el-select v-else id="registered_user" v-model="form['registered_user']" :disabled="disabledObj['registered_user_isDisabled']">
							<el-option v-for="o in list_user_registered_user" :key="o['username']" :label="o['nickname'] + '-' + o['username']"
									   :value="o['user_id']">
							</el-option>
						</el-select>
																</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','user_name') || $check_field('add','user_name') || $check_field('set','user_name')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="用户姓名" prop="user_name">
															<el-input id="user_name" v-model="form['user_name']" placeholder="请输入用户姓名"
							  v-if="(form['skin_detection_id'] && $check_field('set','user_name')) || (!form['skin_detection_id'] && $check_field('add','user_name'))" :disabled="disabledObj['user_name_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','user_name')">{{form['user_name']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','user_gender') || $check_field('add','user_gender') || $check_field('set','user_gender')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="用户性别" prop="user_gender">
															<el-input id="user_gender" v-model="form['user_gender']" placeholder="请输入用户性别"
							  v-if="(form['skin_detection_id'] && $check_field('set','user_gender')) || (!form['skin_detection_id'] && $check_field('add','user_gender'))" :disabled="disabledObj['user_gender_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','user_gender')">{{form['user_gender']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','user_age') || $check_field('add','user_age') || $check_field('set','user_age')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="用户年龄" prop="user_age">
											<el-input-number id="user_age" v-model.number="form['user_age']"
						v-if="(form['skin_detection_id'] && $check_field('set','user_age')) || (!form['skin_detection_id'] && $check_field('add','user_age'))" :disabled="disabledObj['user_age_isDisabled']"></el-input-number>
					<div v-else-if="$check_field('get','user_age')">{{form['user_age']}}</div>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','entry_date') || $check_field('add','entry_date') || $check_field('set','entry_date')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="录入日期" prop="entry_date">
											<el-date-picker :disabled="disabledObj['entry_date_isDisabled']" v-if="(form['skin_detection_id'] && $check_field('set','entry_date')) || (!form['skin_detection_id'] && $check_field('add','entry_date'))" id="entry_date"
						v-model="form['entry_date']" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
					</el-date-picker>
					<div v-else-if="$check_field('get','entry_date')">{{form['entry_date']}}</div>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','detection_picture') || $check_field('add','detection_picture') || $check_field('set','detection_picture')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="检测图片" prop="detection_picture">
											<el-upload :disabled="disabledObj['detection_picture_isDisabled']" class="avatar-uploader" drag
						accept="image/gif, image/jpeg, image/png, image/jpg" action="" :http-request="upload_detection_picture"
						:show-file-list="false" v-if="(form['skin_detection_id'] && $check_field('set','detection_picture')) || (!form['skin_detection_id'] && $check_field('add','detection_picture'))">
						<img id="detection_picture" v-if="form['detection_picture']" :src="$fullUrl(form['detection_picture'])" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
					<el-image v-else-if="$check_field('get','detection_picture')" style="width: 100px; height: 100px"
						:src="$fullUrl(form['detection_picture'])" :preview-src-list="[$fullUrl(form['detection_picture'])]">
						<div slot="error" class="image-slot">
							<img src="../../../public/img/error.png" style="width: 90px; height: 90px" />
						</div>
					</el-image>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','skin_condition') || $check_field('add','skin_condition') || $check_field('set','skin_condition')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="皮肤情况" prop="skin_condition">
											<el-input type="textarea" id="skin_condition" v-model="form['skin_condition']" placeholder="请输入皮肤情况"
						v-if="(form['skin_detection_id'] && $check_field('set','skin_condition')) || (!form['skin_detection_id'] && $check_field('add','skin_condition'))" :disabled="disabledObj['skin_condition_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','skin_condition')">{{form['skin_condition']}}</div>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','test_results') || $check_field('add','test_results') || $check_field('set','test_results')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="检测结果" prop="test_results">
															<el-input id="test_results" v-model="form['test_results']" placeholder="请输入检测结果"
							  v-if="(form['skin_detection_id'] && $check_field('set','test_results')) || (!form['skin_detection_id'] && $check_field('add','test_results'))" :disabled="disabledObj['test_results_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','test_results')">{{form['test_results']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','test_description') || $check_field('add','test_description') || $check_field('set','test_description')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="检测说明" prop="test_description">
											<el-input type="textarea" id="test_description" v-model="form['test_description']" placeholder="请输入检测说明"
						v-if="(form['skin_detection_id'] && $check_field('set','test_description')) || (!form['skin_detection_id'] && $check_field('add','test_description'))" :disabled="disabledObj['test_description_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','test_description')">{{form['test_description']}}</div>
							</el-form-item>
			</el-col>
						
	
	
		
		
	
	
	
		
		
	
		</el-row>
			<el-col :xs="24" :sm="12" :lg="8" class="el_form_btn_warp">
				<el-form-item v-if="$check_action('/skin_detection/view','set') || $check_action('/skin_detection/view','add') || $check_option('/skin_detection/table','examine')">
					<el-button type="primary" @click="submit()">提交</el-button>
					<el-button @click="cancel()">取消</el-button>
				</el-form-item>
				<el-form-item v-else>
					<el-button @click="cancel()">返回</el-button>
				</el-form-item>
			</el-col>

		</el-form>
	</el-main>
</template>

<script>
	import mixin from "@/mixins/page.js";
	export default {
		mixins: [mixin],
		data() {
			return {
				field: "skin_detection_id",
				url_add: "~/api/skin_detection/add?",
				url_set: "~/api/skin_detection/set?",
				url_get_obj: "~/api/skin_detection/get_obj?",
				url_upload: "~/api/skin_detection/upload?",

				query: {
					"skin_detection_id": 0,
				},

				form: {
								"registered_user": 0, // 注册用户
										"user_name":  '', // 用户姓名
										"user_gender":  '', // 用户性别
										"user_age":  0, // 用户年龄
										"entry_date":  '', // 录入日期
										"detection_picture":  '', // 检测图片
										"skin_condition":  '', // 皮肤情况
										"test_results":  '', // 检测结果
										"test_description":  '', // 检测说明
											"skin_detection_id": 0, // ID
															},
				disabledObj:{
								"registered_user_isDisabled": false,
										"user_name_isDisabled": false,
										"user_gender_isDisabled": false,
					          			"user_age_isDisabled": false,
										"entry_date_isDisabled": false,
										"detection_picture_isDisabled": false,
										"skin_condition_isDisabled": false,
										"test_results_isDisabled": false,
										"test_description_isDisabled": false,
										},

	
					// 用户列表
				list_user_registered_user: [],
						// 用户组
				group_user_registered_user: "",
					
			
			
			
			
			
			
			
		
			}
		},
		methods: {

	
	
				/**
			 * 获取注册用户用户列表
			 */
			async get_list_user_registered_user() {
                var json = await this.$get("~/api/user/get_list?user_group=注册用户");
                if(json.result && json.result.list){
                    this.list_user_registered_user = json.result.list;
                }
                else if(json.error){
                    console.error(json.error);
                }
			},
					/**
			 * 获取注册用户用户组
			 */
			async get_group_user_registered_user() {
							this.form["registered_user"] = this.user.user_id;
							var json = await this.$get("~/api/user_group/get_obj?name=注册用户");
				if(json.result && json.result.obj){
					this.group_user_registered_user = json.result.obj;
				}
				else if(json.error){
					console.error(json.error);
				}
			},
			get_user_session_registered_user(id){
				var _this = this;
				var user_id = {"user_id":id}
				var url = "~/api/"+_this.group_user_registered_user.source_table+"/get_obj?"
				this.$get(url, user_id, function(res) {
					if (res.result && res.result.obj) {
						var arr = []
						for (let key in res.result.obj) {
							arr.push(key)
						}
						var arrForm = []
									for (let key in _this.form) {
							arrForm.push(key)
						}
												_this.form["registered_user"] = id
									_this.disabledObj['registered_user' + '_isDisabled'] = true
						for (var i=0;i<arr.length;i++){
						  if (arr[i]!=='examine_state' && arr[i]!=='examine_reply') {
							for (var j = 0; j < arrForm.length; j++) {
							  if (arr[i] === arrForm[j]) {
								if (arr[i] !== "registered_user") {
			                      _this.form[arrForm[j]] = res.result.obj[arr[i]]
			                      _this.disabledObj[arrForm[j] + '_isDisabled'] = true
								  break;
								} else {
								  _this.disabledObj[arrForm[j] + '_isDisabled'] = true
								}
							  }
							}
						  }
						}
					}
				});
			},
					get_user_registered_user(id){
				var obj = this.list_user_registered_user.getObj({"user_id":id});
				var ret = "";
				if(obj){
					if(obj.nickname){
						ret = obj.nickname;}
					else{
						ret = obj.username;
					}
				}
				return ret;
			},
			
	
			
	
			
	
			
	
						/**
			 * 上传检测图片
			 * @param {Object} param 图片参数
			 */
			upload_detection_picture(param){
									this.uploadFile(param.file, "detection_picture");
								},
	
	
			
	
			
	
			
	
		
			/**
			 * 获取对象之前
			 * @param {Object} param
			 */
			get_obj_before(param) {
				var form = "";
					
				if(this.form && form){
					Object.keys(this.form).forEach(key => {
						Object.keys(form).forEach(dbKey => {
							// if(dbKey === "charging_standard"){
							// 	this.form['charging_rules'] = form[dbKey];
							// 	this.disabledObj['charging_rules_isDisabled'] = true;
							// };
							if(key === dbKey){
								this.disabledObj[key+'_isDisabled'] = true;
								this.form[key] = form[dbKey]
							}
							if(dbKey === "source_table"){
								this.form['source_table'] = form[dbKey];
							}
							if(dbKey === "source_id"){
								this.form['source_id'] = form[dbKey];
							}
							if(dbKey === "source_user_id"){
								this.form['source_user_id'] = form[dbKey];
							}
						})
					})
				}
					        if (this.form["entry_date"] && this.form["entry_date"].indexOf("-")===-1){
          this.form["entry_date"] = this.$toTime(parseInt(this.form["entry_date"]),"yyyy-MM-dd")
        }
									$.db.del("form");

				return param;
			},

			/**
			 * 获取对象之后
			 * @param {Object} json
			 * @param {Object} func
			 */
			get_obj_after(json, func){
																										if(this.form["entry_date"]=="0000-00-00"){
				  this.form["entry_date"] = null;
				}
				if(parseInt(this.form["entry_date"]) > 9999){
					this.form["entry_date"] = this.$toTime(parseInt(this.form["entry_date"]),"yyyy-MM-dd")
				}
																								

			},

																																																																											async submit(param, func){
				if (!param) {
					param = this.form;
				}
						
				var pm = this.events("submit_before", Object.assign({}, param)) || param;
				var msg = await this.events("submit_check", pm);
				var ret;
				if (msg) {
					this.$toast(msg, 'danger');
				} else {
																																																																							ret = this.events("submit_main", pm, func);
				}
				return ret;
			},
			
			/**
			 * 提交前验证事件
			 * @param {Object} 请求参数
			 * @return {String} 验证成功返回null, 失败返回错误提示
			 */
						submit_check(param) {
					
																																																			if (!param.entry_date){
					return "录入日期不能为空";
				}
																																																												return null;
			},

			is_view(){
				// var bl = this.user_group == "管理员";
				var bl = false;

				if(!bl){
					bl = this.$check_action('/skin_detection/table','add');
					console.log(bl ? "你有表格添加权限视作有添加权限" : "你没有表格添加权限");
				}
				if(!bl){
					bl = this.$check_action('/skin_detection/table','set');
					console.log(bl ? "你有表格添加权限视作有修改权限" : "你没有表格修改权限");
				}
				if(!bl){
					bl = this.$check_action('/skin_detection/view','add');
					console.log(bl ? "你有视图添加权限视作有添加权限" : "你没有视图添加权限");
				}
				if(!bl){
					bl = this.$check_action('/skin_detection/view','set');
					console.log(bl ? "你有视图修改权限视作有修改权限" : "你没有视图修改权限");
				}
				if(!bl){
					bl = this.$check_action('/skin_detection/view','get');
					console.log(bl ? "你有视图查询权限视作有查询权限" : "你没有视图查询权限");
				}

				console.log(bl ? "具有当前页面的查看权，请注意这不代表你有字段的查看权" : "无权查看当前页，请注意即便有字段查询权限没有页面查询权限也不行");

				return bl;
			},
			/**
			 * 上传文件
			 * @param {Object} param
			 */
			uploadimg(param) {
				this.uploadFile(param.file, "avatar");
			},

		},
		created() {
					this.get_list_user_registered_user();
					this.get_group_user_registered_user();
																					},
	}
</script>

<style>
	.avatar-uploader .el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}

	.avatar-uploader .el-upload:hover {
		border-color: #409EFF;
	}

	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 178px;
		height: 178px;
		line-height: 178px;
		text-align: center;
	}

	.avatar {
		width: 178px;
		height: 178px;
		display: block;
	}
	
	.img_multiple{
		overflow: hidden;
	}
	.img_multiple .img_block{
		float: left;
		margin-right: 5px;
		margin-bottom: 5px;
		position: relative;
	}
	.img_multiple .img_block img{
		height: 100px;
		width: auto;
	}
	.img_multiple .img_del{
		position: absolute;
		top: 5px;
		right: 5px;
		width: 20px;
		height: 20px;
		background: #0000008a;
		color: #fff;
		line-height: 20px;
		text-align: center;
		border-radius: 100%;
		cursor: pointer;
	}




	
</style>
