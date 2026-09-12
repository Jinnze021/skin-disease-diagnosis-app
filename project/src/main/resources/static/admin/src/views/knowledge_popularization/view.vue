<template>
	<el-main class="bg edit_wrap comtable_e">
		<el-form ref="form" :model="form" status-icon label-width="120px" v-if="is_view()">
		<el-row class="row_ce"> 
							<el-col v-if="$check_field('get','name_of_skin_disease') || $check_field('add','name_of_skin_disease') || $check_field('set','name_of_skin_disease')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="皮肤病名称" prop="name_of_skin_disease">
															<el-input id="name_of_skin_disease" v-model="form['name_of_skin_disease']" placeholder="请输入皮肤病名称"
							  v-if="(form['knowledge_popularization_id'] && $check_field('set','name_of_skin_disease')) || (!form['knowledge_popularization_id'] && $check_field('add','name_of_skin_disease'))" :disabled="disabledObj['name_of_skin_disease_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','name_of_skin_disease')">{{form['name_of_skin_disease']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','types_of_skin_diseases') || $check_field('add','types_of_skin_diseases') || $check_field('set','types_of_skin_diseases')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="皮肤病种类" prop="types_of_skin_diseases">
															<el-input id="types_of_skin_diseases" v-model="form['types_of_skin_diseases']" placeholder="请输入皮肤病种类"
							  v-if="(form['knowledge_popularization_id'] && $check_field('set','types_of_skin_diseases')) || (!form['knowledge_popularization_id'] && $check_field('add','types_of_skin_diseases'))" :disabled="disabledObj['types_of_skin_diseases_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','types_of_skin_diseases')">{{form['types_of_skin_diseases']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','release_date') || $check_field('add','release_date') || $check_field('set','release_date')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="发布日期" prop="release_date">
											<el-date-picker :disabled="disabledObj['release_date_isDisabled']" v-if="(form['knowledge_popularization_id'] && $check_field('set','release_date')) || (!form['knowledge_popularization_id'] && $check_field('add','release_date'))" id="release_date"
						v-model="form['release_date']" type="date" placeholder="选择日期" value-format="yyyy-MM-dd">
					</el-date-picker>
					<div v-else-if="$check_field('get','release_date')">{{form['release_date']}}</div>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','treatment_mode') || $check_field('add','treatment_mode') || $check_field('set','treatment_mode')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="治疗方式" prop="treatment_mode">
															<el-input id="treatment_mode" v-model="form['treatment_mode']" placeholder="请输入治疗方式"
							  v-if="(form['knowledge_popularization_id'] && $check_field('set','treatment_mode')) || (!form['knowledge_popularization_id'] && $check_field('add','treatment_mode'))" :disabled="disabledObj['treatment_mode_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','treatment_mode')">{{form['treatment_mode']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','popular_science_pictures') || $check_field('add','popular_science_pictures') || $check_field('set','popular_science_pictures')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="科普图片" prop="popular_science_pictures">
											<el-upload :disabled="disabledObj['popular_science_pictures_isDisabled']" class="avatar-uploader" drag
						accept="image/gif, image/jpeg, image/png, image/jpg" action="" :http-request="upload_popular_science_pictures"
						:show-file-list="false" v-if="(form['knowledge_popularization_id'] && $check_field('set','popular_science_pictures')) || (!form['knowledge_popularization_id'] && $check_field('add','popular_science_pictures'))">
						<img id="popular_science_pictures" v-if="form['popular_science_pictures']" :src="$fullUrl(form['popular_science_pictures'])" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
					<el-image v-else-if="$check_field('get','popular_science_pictures')" style="width: 100px; height: 100px"
						:src="$fullUrl(form['popular_science_pictures'])" :preview-src-list="[$fullUrl(form['popular_science_pictures'])]">
						<div slot="error" class="image-slot">
							<img src="../../../public/img/error.png" style="width: 90px; height: 90px" />
						</div>
					</el-image>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','popular_science_video') || $check_field('add','popular_science_video') || $check_field('set','popular_science_video')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="科普视频" prop="popular_science_video">
															<el-upload v-if="(form['knowledge_popularization_id'] && $check_field('set','popular_science_video')) || (!form['knowledge_popularization_id'] && $check_field('add','popular_science_video'))" class="upload-demo" drag
						action="" style="max-width: 300px;width: 100%;" :http-request="upload_popular_science_video" :limit="1" accept="video/ogg,video/mp4,video/webm">
						<i class="el-icon-upload"></i>
						<div class="el-upload__text">将视频拖到此处，或<em>点击上传</em></div>
					</el-upload>
					<div v-else-if="$check_field('get','popular_science_video')">
						<el-button type="primary" @click="download(form['popular_science_video'])">下载<i
								class="el-icon-download el-icon--right"></i></el-button>
					</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','skin_disease_introduction') || $check_field('add','skin_disease_introduction') || $check_field('set','skin_disease_introduction')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="皮肤病介绍" prop="skin_disease_introduction">
											<el-input type="textarea" id="skin_disease_introduction" v-model="form['skin_disease_introduction']" placeholder="请输入皮肤病介绍"
						v-if="(form['knowledge_popularization_id'] && $check_field('set','skin_disease_introduction')) || (!form['knowledge_popularization_id'] && $check_field('add','skin_disease_introduction'))" :disabled="disabledObj['skin_disease_introduction_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','skin_disease_introduction')">{{form['skin_disease_introduction']}}</div>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','treatment_plan') || $check_field('add','treatment_plan') || $check_field('set','treatment_plan')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="治疗方案" prop="treatment_plan">
											<el-input type="textarea" id="treatment_plan" v-model="form['treatment_plan']" placeholder="请输入治疗方案"
						v-if="(form['knowledge_popularization_id'] && $check_field('set','treatment_plan')) || (!form['knowledge_popularization_id'] && $check_field('add','treatment_plan'))" :disabled="disabledObj['treatment_plan_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','treatment_plan')">{{form['treatment_plan']}}</div>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','science_popularization_details') || $check_field('add','science_popularization_details') || $check_field('set','science_popularization_details')" :xs="24" :sm="24" :lg="24" class="el_form_editor_warp">
							<el-form-item label="科普详情" prop="science_popularization_details">
								<quill-editor v-model.number="form['science_popularization_details']"
						v-if="(form['knowledge_popularization_id'] && $check_field('set','science_popularization_details')) || (!form['knowledge_popularization_id'] && $check_field('add','science_popularization_details')) ">
					</quill-editor>
					<div v-else-if="$check_field('get','science_popularization_details')" v-html="form['science_popularization_details']"></div>
				</el-form-item>
			</el-col>
						
	
	
		
		
	
	
	
		
		
	
		</el-row>
			<el-col :xs="24" :sm="12" :lg="8" class="el_form_btn_warp">
				<el-form-item v-if="$check_action('/knowledge_popularization/view','set') || $check_action('/knowledge_popularization/view','add') || $check_option('/knowledge_popularization/table','examine')">
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
				field: "knowledge_popularization_id",
				url_add: "~/api/knowledge_popularization/add?",
				url_set: "~/api/knowledge_popularization/set?",
				url_get_obj: "~/api/knowledge_popularization/get_obj?",
				url_upload: "~/api/knowledge_popularization/upload?",

				query: {
					"knowledge_popularization_id": 0,
				},

				form: {
								"name_of_skin_disease":  '', // 皮肤病名称
										"types_of_skin_diseases":  '', // 皮肤病种类
										"release_date":  '', // 发布日期
										"treatment_mode":  '', // 治疗方式
										"popular_science_pictures":  '', // 科普图片
										"popular_science_video":  '', // 科普视频
										"skin_disease_introduction":  '', // 皮肤病介绍
										"treatment_plan":  '', // 治疗方案
										"science_popularization_details":  '', // 科普详情
											"knowledge_popularization_id": 0, // ID
															},
				disabledObj:{
								"name_of_skin_disease_isDisabled": false,
										"types_of_skin_diseases_isDisabled": false,
										"release_date_isDisabled": false,
										"treatment_mode_isDisabled": false,
										"popular_science_pictures_isDisabled": false,
										"popular_science_video_isDisabled": false,
										"skin_disease_introduction_isDisabled": false,
										"treatment_plan_isDisabled": false,
										"science_popularization_details_isDisabled": false,
										},

	
			
			
			
			
			
			
			
			
		
			}
		},
		methods: {

	
	
			
	
			
	
			
	
						/**
			 * 上传科普图片
			 * @param {Object} param 图片参数
			 */
			upload_popular_science_pictures(param){
									this.uploadFile(param.file, "popular_science_pictures");
								},
	
	
						/**
			 * 上传科普视频
			 * @param {Object} param 视频参数
			 */
			upload_popular_science_video(param){
									this.uploadFile(param.file, "popular_science_video");
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
			        if (this.form["release_date"] && this.form["release_date"].indexOf("-")===-1){
          this.form["release_date"] = this.$toTime(parseInt(this.form["release_date"]),"yyyy-MM-dd")
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
																if(this.form["release_date"]=="0000-00-00"){
				  this.form["release_date"] = null;
				}
				if(parseInt(this.form["release_date"]) > 9999){
					this.form["release_date"] = this.$toTime(parseInt(this.form["release_date"]),"yyyy-MM-dd")
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
					
																													if (!param.release_date){
					return "发布日期不能为空";
				}
																																																																																		return null;
			},

			is_view(){
				// var bl = this.user_group == "管理员";
				var bl = false;

				if(!bl){
					bl = this.$check_action('/knowledge_popularization/table','add');
					console.log(bl ? "你有表格添加权限视作有添加权限" : "你没有表格添加权限");
				}
				if(!bl){
					bl = this.$check_action('/knowledge_popularization/table','set');
					console.log(bl ? "你有表格添加权限视作有修改权限" : "你没有表格修改权限");
				}
				if(!bl){
					bl = this.$check_action('/knowledge_popularization/view','add');
					console.log(bl ? "你有视图添加权限视作有添加权限" : "你没有视图添加权限");
				}
				if(!bl){
					bl = this.$check_action('/knowledge_popularization/view','set');
					console.log(bl ? "你有视图修改权限视作有修改权限" : "你没有视图修改权限");
				}
				if(!bl){
					bl = this.$check_action('/knowledge_popularization/view','get');
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
