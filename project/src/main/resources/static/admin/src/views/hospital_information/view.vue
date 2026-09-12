<template>
	<el-main class="bg edit_wrap comtable_e">
		<el-form ref="form" :model="form" status-icon label-width="120px" v-if="is_view()">
		<el-row class="row_ce"> 
							<el-col v-if="$check_field('get','hospital_name') || $check_field('add','hospital_name') || $check_field('set','hospital_name')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="医院名称" prop="hospital_name">
															<el-input id="hospital_name" v-model="form['hospital_name']" placeholder="请输入医院名称"
							  v-if="(form['hospital_information_id'] && $check_field('set','hospital_name')) || (!form['hospital_information_id'] && $check_field('add','hospital_name'))" :disabled="disabledObj['hospital_name_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','hospital_name')">{{form['hospital_name']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','hospital_address') || $check_field('add','hospital_address') || $check_field('set','hospital_address')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="医院地址" prop="hospital_address">
															<el-input id="hospital_address" v-model="form['hospital_address']" placeholder="请输入医院地址"
							  v-if="(form['hospital_information_id'] && $check_field('set','hospital_address')) || (!form['hospital_information_id'] && $check_field('add','hospital_address'))" :disabled="disabledObj['hospital_address_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','hospital_address')">{{form['hospital_address']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','type_of_hospital') || $check_field('add','type_of_hospital') || $check_field('set','type_of_hospital')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="医院类型" prop="type_of_hospital">
															<el-input id="type_of_hospital" v-model="form['type_of_hospital']" placeholder="请输入医院类型"
							  v-if="(form['hospital_information_id'] && $check_field('set','type_of_hospital')) || (!form['hospital_information_id'] && $check_field('add','type_of_hospital'))" :disabled="disabledObj['type_of_hospital_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','type_of_hospital')">{{form['type_of_hospital']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','distance_from_hospital') || $check_field('add','distance_from_hospital') || $check_field('set','distance_from_hospital')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="医院距离" prop="distance_from_hospital">
															<el-input id="distance_from_hospital" v-model="form['distance_from_hospital']" placeholder="请输入医院距离"
							  v-if="(form['hospital_information_id'] && $check_field('set','distance_from_hospital')) || (!form['hospital_information_id'] && $check_field('add','distance_from_hospital'))" :disabled="disabledObj['distance_from_hospital_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','distance_from_hospital')">{{form['distance_from_hospital']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','hospital_phone_number') || $check_field('add','hospital_phone_number') || $check_field('set','hospital_phone_number')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="医院电话" prop="hospital_phone_number">
															<el-input id="hospital_phone_number" v-model="form['hospital_phone_number']" placeholder="请输入医院电话"
							  v-if="(form['hospital_information_id'] && $check_field('set','hospital_phone_number')) || (!form['hospital_information_id'] && $check_field('add','hospital_phone_number'))" :disabled="disabledObj['hospital_phone_number_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','hospital_phone_number')">{{form['hospital_phone_number']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','hospital_score') || $check_field('add','hospital_score') || $check_field('set','hospital_score')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="医院评分" prop="hospital_score">
															<el-input id="hospital_score" v-model="form['hospital_score']" placeholder="请输入医院评分"
							  v-if="(form['hospital_information_id'] && $check_field('set','hospital_score')) || (!form['hospital_information_id'] && $check_field('add','hospital_score'))" :disabled="disabledObj['hospital_score_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','hospital_score')">{{form['hospital_score']}}</div>
											</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','hospital_pictures') || $check_field('add','hospital_pictures') || $check_field('set','hospital_pictures')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="医院图片" prop="hospital_pictures">
											<el-upload :disabled="disabledObj['hospital_pictures_isDisabled']" class="avatar-uploader" drag
						accept="image/gif, image/jpeg, image/png, image/jpg" action="" :http-request="upload_hospital_pictures"
						:show-file-list="false" v-if="(form['hospital_information_id'] && $check_field('set','hospital_pictures')) || (!form['hospital_information_id'] && $check_field('add','hospital_pictures'))">
						<img id="hospital_pictures" v-if="form['hospital_pictures']" :src="$fullUrl(form['hospital_pictures'])" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
					<el-image v-else-if="$check_field('get','hospital_pictures')" style="width: 100px; height: 100px"
						:src="$fullUrl(form['hospital_pictures'])" :preview-src-list="[$fullUrl(form['hospital_pictures'])]">
						<div slot="error" class="image-slot">
							<img src="../../../public/img/error.png" style="width: 90px; height: 90px" />
						</div>
					</el-image>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','areas_of_expertise') || $check_field('add','areas_of_expertise') || $check_field('set','areas_of_expertise')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="擅长领域" prop="areas_of_expertise">
											<el-input type="textarea" id="areas_of_expertise" v-model="form['areas_of_expertise']" placeholder="请输入擅长领域"
						v-if="(form['hospital_information_id'] && $check_field('set','areas_of_expertise')) || (!form['hospital_information_id'] && $check_field('add','areas_of_expertise'))" :disabled="disabledObj['areas_of_expertise_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','areas_of_expertise')">{{form['areas_of_expertise']}}</div>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','hospital_introduction') || $check_field('add','hospital_introduction') || $check_field('set','hospital_introduction')" :xs="24" :sm="12" :lg="8" class="el_form_item_warp">
							<el-form-item label="医院介绍" prop="hospital_introduction">
											<el-input type="textarea" id="hospital_introduction" v-model="form['hospital_introduction']" placeholder="请输入医院介绍"
						v-if="(form['hospital_information_id'] && $check_field('set','hospital_introduction')) || (!form['hospital_information_id'] && $check_field('add','hospital_introduction'))" :disabled="disabledObj['hospital_introduction_isDisabled']"></el-input>
					<div v-else-if="$check_field('get','hospital_introduction')">{{form['hospital_introduction']}}</div>
							</el-form-item>
			</el-col>
								<el-col v-if="$check_field('get','hospital_details') || $check_field('add','hospital_details') || $check_field('set','hospital_details')" :xs="24" :sm="24" :lg="24" class="el_form_editor_warp">
							<el-form-item label="医院详情" prop="hospital_details">
								<quill-editor v-model.number="form['hospital_details']"
						v-if="(form['hospital_information_id'] && $check_field('set','hospital_details')) || (!form['hospital_information_id'] && $check_field('add','hospital_details')) ">
					</quill-editor>
					<div v-else-if="$check_field('get','hospital_details')" v-html="form['hospital_details']"></div>
				</el-form-item>
			</el-col>
						
	
	
		
		
	
	
	
		
		
	
		</el-row>
			<el-col :xs="24" :sm="12" :lg="8" class="el_form_btn_warp">
				<el-form-item v-if="$check_action('/hospital_information/view','set') || $check_action('/hospital_information/view','add') || $check_option('/hospital_information/table','examine')">
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
				field: "hospital_information_id",
				url_add: "~/api/hospital_information/add?",
				url_set: "~/api/hospital_information/set?",
				url_get_obj: "~/api/hospital_information/get_obj?",
				url_upload: "~/api/hospital_information/upload?",

				query: {
					"hospital_information_id": 0,
				},

				form: {
								"hospital_name":  '', // 医院名称
										"hospital_address":  '', // 医院地址
										"type_of_hospital":  '', // 医院类型
										"distance_from_hospital":  '', // 医院距离
										"hospital_phone_number":  '', // 医院电话
										"hospital_score":  '', // 医院评分
										"hospital_pictures":  '', // 医院图片
										"areas_of_expertise":  '', // 擅长领域
										"hospital_introduction":  '', // 医院介绍
										"hospital_details":  '', // 医院详情
											"hospital_information_id": 0, // ID
															},
				disabledObj:{
								"hospital_name_isDisabled": false,
										"hospital_address_isDisabled": false,
										"type_of_hospital_isDisabled": false,
										"distance_from_hospital_isDisabled": false,
										"hospital_phone_number_isDisabled": false,
										"hospital_score_isDisabled": false,
										"hospital_pictures_isDisabled": false,
										"areas_of_expertise_isDisabled": false,
										"hospital_introduction_isDisabled": false,
										"hospital_details_isDisabled": false,
										},

	
			
			
			
			
			
			
			
			
			
		
			}
		},
		methods: {

	
	
			
	
			
	
			
	
			
	
			
	
						/**
			 * 上传医院图片
			 * @param {Object} param 图片参数
			 */
			upload_hospital_pictures(param){
									this.uploadFile(param.file, "hospital_pictures");
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
														$.db.del("form");

				return param;
			},

			/**
			 * 获取对象之后
			 * @param {Object} json
			 * @param {Object} func
			 */
			get_obj_after(json, func){
																																																		

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
					
																																																																																																																				return null;
			},

			is_view(){
				// var bl = this.user_group == "管理员";
				var bl = false;

				if(!bl){
					bl = this.$check_action('/hospital_information/table','add');
					console.log(bl ? "你有表格添加权限视作有添加权限" : "你没有表格添加权限");
				}
				if(!bl){
					bl = this.$check_action('/hospital_information/table','set');
					console.log(bl ? "你有表格添加权限视作有修改权限" : "你没有表格修改权限");
				}
				if(!bl){
					bl = this.$check_action('/hospital_information/view','add');
					console.log(bl ? "你有视图添加权限视作有添加权限" : "你没有视图添加权限");
				}
				if(!bl){
					bl = this.$check_action('/hospital_information/view','set');
					console.log(bl ? "你有视图修改权限视作有修改权限" : "你没有视图修改权限");
				}
				if(!bl){
					bl = this.$check_action('/hospital_information/view','get');
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
