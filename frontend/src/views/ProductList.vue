<template>
<v-container>
    <v-row justify="center">
        <v-col cols="9">
            <v-row class="text-h4">
                BEST ITEMS
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="(product, idx) in bestProducts" :key="idx" cols="3">
                            <v-card @click="moveToDetail(product.productNo)">
                                <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(idx)}/300/250`"></v-img>
                                <v-card-text>
                                    <div>
                                        {{product.productName}}
                                        - <span v-if="product.size != null">{{product.size.split(';').length-1}} size</span>
                                        <span v-if="product.size == null">{{product.color.split(';').length-1}} color</span>
                                    </div>
                                    <div v-if="product.discount != 0" class="text-decoration-line-through">{{product.price}}원</div>
                                    <div v-if="product.discount == 0">{{product.price}}원</div>
                                    <div v-if="product.discount != 0">{{product.price-product.discount}}원</div>
                                </v-card-text>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
            <v-divider></v-divider>
            <v-row class="text-h4 mt-10 text-uppercase">
                {{mainCategory}}
            </v-row>
            <v-row>
                <v-col cols="auto" v-for="category in subCategory" :key="category.value">
                    <v-btn :to="`/productList/${mainCategory}/${category.value}`">
                        {{category.text}}
                    </v-btn>
                </v-col>
            </v-row>
            <v-row class="my-10" justify="center">
                <v-col>
                    <v-row>
                        <v-col v-for="(product, idx) in products" :key="idx" cols="3">
                            <v-card @click="moveToDetail(product.productNo)">
                                <v-img max-height="300" max-width="auto" :src="`https://picsum.photos/seed/${randomNumber(idx)}/300/250`"></v-img>
                                <v-card-text>
                                    <div>
                                        {{product.productName}}
                                        - <span v-if="product.size != null">{{product.size.split(';').length-1}} size</span>
                                        <span v-if="product.size == null">{{product.color.split(';').length-1}} color</span>
                                    </div>
                                    <div v-if="product.discount != 0" class="text-decoration-line-through">{{product.price}}원</div>
                                    <div v-if="product.discount == 0">{{product.price}}원</div>
                                    <div v-if="product.discount != 0">{{product.price-product.discount}}원</div>
                                </v-card-text>
                            </v-card>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
            <div class="text-center">
                <v-pagination v-model="page" :length="pageLength" :total-visible="visibleLength"></v-pagination>
            </div>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
import axios from 'axios'
export default {
    data() {
        return {
            mainCategory: 'SKIRT',
            subCategory: '',
            selectedCategory: 'all',

            page: 1,
            itemsPerPage: 12,
            pageLength: 7,
            visibleLength: 5,

            bestProducts: [],
            products: [],
        }
    },
    methods: {
        setSubCategory() {
            this.subCategory = [{
                text: 'ALL',
                value: 'all',
                type: 'all'
            }, {
                text: '셔츠',
                value: 'shirt',
                type: 'shirt'
            }, {
                text: '블라우스',
                value: 'blouse',
                type: 'shirt'
            }, {
                text: '미니',
                value: 'mini',
                type: 'skirt'
            }, {
                text: '미디/롱',
                value: 'midi-long',
                type: 'skirt'
            }];
            for (let i = this.subCategory.length - 1; i > 0; i--) {
                if (this.mainCategory != this.subCategory[i].type) {
                    this.subCategory.splice(i, 1);
                }
            }
        },
        getProductList() {
            axios({
                method: 'get',
                url: `/api/product/getBestProductList`,
                params: {
                    type1: this.mainCategory,
                    type2: this.selectedCategory
                }
            }).then(res => {
                this.bestProducts = res.data;
            }).catch((err) => {
                console.log(err);
            })
            axios({
                method: 'get',
                url: `/api/product/getProductList`,
                params: {
                    page: this.page,
                    perPage: this.itemsPerPage,
                    type1: this.mainCategory,
                    type2: this.selectedCategory,
                    minPrice: 0,
                    maxPrice: 99999999,
                }
            }).then(res => {
                console.log(res.data);
                this.products = res.data.productList;
                this.pageLength = Math.ceil(res.data.count / this.page);
                // this.products = res.data;
                // axios({
                //     method: 'get',
                //     url: `/api/product/getProductCountByType`,
                //     params: {
                //         type1: this.mainCategory,
                //         type2: this.selectedCategory,
                //         minPrice: 0,
                //         maxPrice: 99999999,
                //     }
                // }).then(res => {
                //     this.pageLength = Math.ceil(res.data / this.page);
                // }).catch((err) => {
                //     console.log(err);
                // })
            }).catch((err) => {
                console.log(err);
            })
        },
        moveToDetail(num) {
            this.$router.push(`/productDetail/${num}`)
        },
        randomNumber(count) {
            return Math.floor(Math.random() * 100) + count;
        }
    },
    watch: {
        '$route'() {
            this.mainCategory = this.$route.params.id;
            this.selectedCategory = this.$route.params.sub;
            this.getProductList();
            this.setSubCategory();
        }
    },
    mounted() {
        this.mainCategory = this.$route.params.id;
        this.selectedCategory = this.$route.params.sub;
        this.getProductList();
        this.setSubCategory();
    }
}
</script>

<style>

</style>
