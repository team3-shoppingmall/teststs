<template>
    <v-container>
    
        <div>
    
            <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" class="elevation-1" item-key="qnaNo" @click:row="moveto" disable-sort>
    
                <template #[`item.type`]="{item}">
    
                    <div class="text-left">
    
                        <QnATitleDisplay :type="item.type" />
    
                    </div>
</template>
<template #[`item.id`]="{item}">
    <div class="text-left">
    
        <HideId :id="item.id" />
    
    </div>
</template>
<template #[`item.regDate`]="{item}">
    <div>
    
        <DateDisplay :regDate="item.regDate" />
    
    </div>
</template>
        </v-data-table>
    </div>

    <v-row align="center" justify="space-between">
        <v-col cols="8" sm="7" md="6" lg="5" xl="4">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="search"></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field v-model="searchWord"></v-text-field>
                </v-col>
                <v-col cols="1" class="mt-3">
                    <v-btn icon @click="getQnA">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col cols="auto">
            <v-btn :to="'/writePost/deliveryQnA'" outlined>글쓰기</v-btn>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
import HideId from '@/components/HideId.vue'
import DateDisplay from '@/components/DateDisplay.vue'
import QnATitleDisplay from '@/components/QnATitleDisplay.vue'
export default {
    components: {
        HideId,
        DateDisplay,
        QnATitleDisplay,
    },
    data() {
        return {
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            headers: [{
                    text: '번호',
                    value: 'qnaNo',
                    sortable: false,
                    width: '10%',
                    align: 'center',
                    divider: true,
                },
                {
                    text: '제목',
                    value: 'type',
                    sortable: false,
                    width: '65%',
                    align: 'center',
                    divider: true,
                },
                {
                    text: '작성자',
                    value: 'id',
                    sortable: false,
                    width: '10%',
                    align: 'center',
                    divider: true,
                },
                {
                    text: '작성일',
                    value: 'regDate',
                    sortable: false,
                    width: '15%',
                    align: 'center',
                },
            ],
            searches: [{
                    text: '제목',
                    value: 'title'
                },
                {
                    text: '내용',
                    value: 'content'
                },
                {
                    text: '작성자',
                    value: 'id'
                }
            ],
            search: 'id',
            searchWord: '',
        }
    },
    methods: {
        getQnA() {
            this.loading = true
            const {
                page,
                itemsPerPage
            } = this.options
            let link = document.location.href;
            link = link.slice(26, link.length - 3);
            axios.get(`/api/qna/getQnaListByType`, {
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    search: this.search,
                    searchWord: this.searchWord,
                    type: link
                }
            }).then(res => {
                this.contents = res.data.qnaList;
                this.totalContents = res.data.count;
                this.loading = false
                // axios.get('/api/qna/getCount', {
                //         params: {
                //             search: this.search,
                //             searchWord: this.searchWord,
                //             type: link
                //         }
                //     }).then(res => {
                //         this.totalContents = res.data;
                //         this.loading = false
                //     })
            })
        },
        moveto(item) {
            this.$router.push(`/qna/${item.qnaNo}`)
        },
    },
    watch: {
        options: {
            handler() {
                this.getQnA()
            },
            deep: true,
        },
    },
}
</script>

<style scoped>
table td {
    border-right: 1px solid #dddddd;
}

table td:last-child {
    border-right: none
}
</style>
