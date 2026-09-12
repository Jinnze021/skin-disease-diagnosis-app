<template>
	<el-main class="bg table_wrap comtabel_t">
		<el-form label-position="right" :model="query" class="form p_4" label-width="120">
			<el-row class="rows row1">



							<el-col :xs="24" :sm="24" :lg="8" class="el_form_search_wrap">
					<el-form-item label="皮肤病名称">
									<el-input v-model="query.name_of_skin_disease"></el-input>
								</el-form-item>
				</el-col>
									<el-col :xs="24" :sm="24" :lg="8" class="el_form_search_wrap">
					<el-form-item label="皮肤病种类">
									<el-input v-model="query.types_of_skin_diseases"></el-input>
								</el-form-item>
				</el-col>
																									</el-row>
	<el-row class="rows row2">

		<el-col :xs="24" :sm="24" :lg="24" class="search_btn_wrap search_btns">

				<el-col :xs="24" :sm="10" :lg="8" class="search_btn_1 search_btn_wrap_1 btns">

										<el-button type="primary" @click="search()" class="search_btn_find">查询</el-button>
						<el-button @click="reset()" style="margin-right: 74px;" class="search_btn_reset">重置</el-button>
																		

						<el-button v-if="$check_action('/knowledge_popularization/table','del') || $check_action('/knowledge_popularization/view','del')" class="search_btn_del" type="danger" @click="delInfo()">删除</el-button>
								
				</el-col>
		</el-col>
	</el-row >

		</el-form>
				<el-table :data="list" @selection-change="selectionChange" @sort-change="$sortChange" style="width: 100%" id="dataTable">
					<el-table-column fixed type="selection" tooltip-effect="dark" width="55">
			</el-table-column>
				<el-table-column prop="name_of_skin_disease" @sort-change="$sortChange" label="皮肤病名称" 				v-if="$check_field('get','name_of_skin_disease')" min-width="200">
					</el-table-column>
					<el-table-column prop="types_of_skin_diseases" @sort-change="$sortChange" label="皮肤病种类" 				v-if="$check_field('get','types_of_skin_diseases')" min-width="200">
					</el-table-column>
					<el-table-column prop="release_date" @sort-change="$sortChange" label="发布日期" 				v-if="$check_field('get','release_date')" min-width="200">
		                <template slot-scope="scope">
                	{{ $toTime(scope.row["release_date"],"yyyy-MM-dd") }}
                </template>
					</el-table-column>
					<el-table-column prop="treatment_mode" @sort-change="$sortChange" label="治疗方式" 				v-if="$check_field('get','treatment_mode')" min-width="200">
					</el-table-column>
					<el-table-column prop="popular_science_pictures" @sort-change="$sortChange" label="科普图片" 				v-if="$check_field('get','popular_science_pictures')" min-width="200">
						<template slot-scope="scope">
					<el-image style="width: 100px; height: 100px" :src="$fullUrl(scope.row['popular_science_pictures'])"
						:preview-src-list="[$fullUrl(scope.row['popular_science_pictures'])]">
						<div slot="error" class="image-slot">
							<img src="../../../public/img/error.png" style="width: 90px; height: 90px" />
						</div>
					</el-image>
				</template>
					</el-table-column>
					<el-table-column prop="popular_science_video" @sort-change="$sortChange" label="科普视频" 				v-if="$check_field('get','popular_science_video')" min-width="200">
						<template slot-scope="scope">
					<a href="javascript:void(0)" @click="popular_science_videoVideoPlay($fullUrl(scope.row['popular_science_video']))" >点此播放</a>
				</template>
					</el-table-column>
					<el-table-column prop="skin_disease_introduction" @sort-change="$sortChange" label="皮肤病介绍" 				v-if="$check_field('get','skin_disease_introduction')" min-width="200">
					</el-table-column>
					<el-table-column prop="treatment_plan" @sort-change="$sortChange" label="治疗方案" 				v-if="$check_field('get','treatment_plan')" min-width="200">
					</el-table-column>
			


            <el-table-column sortable prop="create_time" label="创建时间" min-width="200">
                <template slot-scope="scope">
                	{{ $toTime(scope.row["create_time"],"yyyy-MM-dd hh:mm:ss") }}
                </template>
            </el-table-column>

			<el-table-column sortable prop="update_time" label="更新时间" min-width="200">
                <template slot-scope="scope">
                	{{ $toTime(scope.row["update_time"],"yyyy-MM-dd hh:mm:ss") }}
                </template>
			</el-table-column>







			<el-table-column fixed="right" label="操作" min-width="200" v-if="$check_action('/knowledge_popularization/table','set') || $check_action('/knowledge_popularization/view','set') || $check_action('/knowledge_popularization/view','get') 
						" >


				<template slot-scope="scope">
					<div class="view_a">
					<router-link class="e-button el-button--small is-plain el-button--success" style="margin: 5px !important;"
					v-if="$check_action('/knowledge_popularization/table','set') || $check_action('/knowledge_popularization/view','set') || $check_action('/knowledge_popularization/view','get')"
						:to="'./view?' + field + '=' + scope.row[field]"
						 size="small">
						<span>详情</span>
					</router-link>
						<router-link v-if="$check_comment('/knowledge_popularization/table')" class="e-button el-button--small is-plain el-button--primary"
								  :to="'../comment/table?size=10&page=1&source_table=knowledge_popularization&source_field=' + field + '&source_id=' + scope.row[field]" size="small">
						<span>查看评论</span>
					</router-link>
					</div>
				</template>
			</el-table-column>

		</el-table>

		<!-- 分页器 -->
		<div class="mt text_center">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:current-page="query.page" :page-sizes="[7, 10, 30, 100]" :page-size="query.size"
				layout="total, sizes, prev, pager, next, jumper" :total="count">
			</el-pagination>
		</div>
		<!-- /分页器 -->
									<el-dialog
					title="视频详情"
					:visible.sync="popular_science_videoPlayerModalVisible"
					width="30%"
			>
				<video id="popular_science_videoPlayer" :src="popular_science_videoVideoUrl" autoplay="autoplay" width="100%" controls>
					您的浏览器不支持Video标签。
				</video>
				<span slot="footer" class="dialog-footer">
				<el-button @click="popular_science_videoCancelModalVisible">取 消</el-button>
				</span>
			</el-dialog>
				
		<div class="modal_wrap" v-if="showModal">
			<div class="modal_box">
				<!-- <div class="modal_box_close" @click="closeModal">X</div> -->
				<p class="modal_box_title">重要提醒</p>
				<p class="modal_box_text">当前有数据达到预警值！</p>
				<p class="modal_box_text">{{ message }}</p>
				<div class="btn_box">
					<span @click="closeModal">取消</span>
					<span @click="closeModal">确定</span>
				</div>
			</div>
		</div>


	</el-main>
</template>
<script>
	import mixin from "@/mixins/page.js";

	export default {
		mixins: [mixin],
		data() {
			return {
				// 弹框
				showModal: false,
				// 获取数据地址
				url_get_list: "~/api/knowledge_popularization/get_list?like=0",
				url_del: "~/api/knowledge_popularization/del?",

				// 字段ID
				field: "knowledge_popularization_id",

				// 查询
				query: {
					"size": 7,
					"page": 1,
								"name_of_skin_disease": "",
											"types_of_skin_diseases": "",
															"login_time": "",
					"create_time": "",
					"orderby": `create_time desc`
				},

				// 数据
				list: [],
										popular_science_videoPlayerModalVisible:false,
				popular_science_videoVideoUrl:"",
																										message: '',
			}
		},
		methods: {
			// 关闭弹框
			closeModal(){
				this.showModal = false;
				},
			/**
			 * @description 获取到列表事件
			 * @param {Object} res 响应结果
			 */
			get_list_after: function get_list_after(res, func, url) {
				let _this = this
																																																				},

																		



															popular_science_videoVideoPlay(v) {
				this.popular_science_videoVideoUrl = v
				this.popular_science_videoPlayerModalVisible = true
			},

			popular_science_videoCancelModalVisible(){
				this.popular_science_videoVideoUrl = ""
				this.popular_science_videoPlayerModalVisible = false
			},
									},
				created() {
																				}
	}
</script>

<style type="text/css">
	.bg {
		background: white;
	}

	.form.p_4 {
		padding: 1rem;
	}

	.form .el-input {
		width: initial;
	}

	.mt {
		margin-top: 1rem;
	}

	.text_center {
		text-align: center;
	}

	.float-right {
		float: right;
	}


	.modal_wrap{
		width: 100vw;
		height: 100vh;
		position: fixed;
		top: 0;
		left: 0;
		background: rgba(0,0,0,0.5);
		z-index: 9999999999;
	}
	.modal_wrap .modal_box{
		width: 400px;
		height: auto;
		background: url("../../assets/modal_bg.jpg") no-repeat center;
		background-size: cover;
		position: absolute;
		top: 50%;
		left: 50%;
		margin-left: -200px;
		margin-top: -100px;
		border-radius: 10px;
		padding: 10px;
		}
	.modal_wrap .modal_box .modal_box_close{
		font-size: 20px;
		position: absolute;
		top: 10px;
		right: 10px;
		cursor: pointer;
		}
	.modal_wrap .modal_box .modal_box_title{
	  text-align: center;
    font-size: 18px;
    margin: 16px auto;
    color: #fff;
    border-bottom: 1px solid rgba(117, 116, 116,0.5);
    padding-bottom: 16px;
    width: 356px;
		}
	.modal_wrap .modal_box .modal_box_text{
		text-align: center;
		font-size: 14px;
		color: #fff;
		margin: 5px auto;
		width: 90%;
		}
	.modal_wrap .modal_box .btn_box{
		display: flex;
		flex-direction: row;
		justify-content: center;
		margin-top: 42px;
		margin-bottom: 20px;
		}
			.modal_wrap .modal_box .btn_box span{
				display: inline-block;
				width: 80px;
				height: 30px;
				line-height: 30px;
				text-align: center;
				border: 1px solid #ccc;
				font-size: 14px;
				cursor: pointer;
				color: #fff;
			}
	.modal_wrap .modal_box .btn_box span:nth-child(2){
		background: #409EFF;
		color: #fff;
		border-color: #409EFF;
		margin-left: 15px;
	}
	.el-date-editor .el-range-separator{
		width: 10% !important;
	}
</style>
