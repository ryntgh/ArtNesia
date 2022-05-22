package com.bangkit.artnesia.data.local

import com.bangkit.artnesia.data.model.ProductModel

object ProductData {
    fun generateProduct(): ArrayList<ProductModel>{
        val product =ArrayList<ProductModel>()

        product.add(
            ProductModel(
                "Product1",
                "123400",
                "lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias consequatur maxime quasi aliquid cum impedit rem. Sunt ipsa eius suscipit excepturi facilis, eum incidunt, itaque reprehenderit obcaecati eos maxime similique minus iusto explicabo rem, ex vitae possimus, tempore totam culpa aliquid fuga sapiente. Dolorum tempora mollitia ipsam laboriosam, esse commodi placeat tenetur reprehenderit alias sit laudantium magni maiores atque illo facere.",
                4.1F,
                "GucciGang",
                "https://i.postimg.cc/tT9x3Psf/1955455.webp",
                "Patung",
            )
        )

        product.add(
            ProductModel(
                "Product2",
                "123400",
                "lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias consequatur maxime quasi aliquid cum impedit rem. Sunt ipsa eius suscipit excepturi facilis, eum incidunt, itaque reprehenderit obcaecati eos maxime similique minus iusto explicabo rem, ex vitae possimus, tempore totam culpa aliquid fuga sapiente. Dolorum tempora mollitia ipsam laboriosam, esse commodi placeat tenetur reprehenderit alias sit laudantium magni maiores atque illo facere.",
                4.2F,
                "ShopJkt",
                "https://i.postimg.cc/cJ7vtL8N/contoh1.jpg",
                "Patung",
            )
        )

        product.add(
            ProductModel(
                "Product3",
                "123400",
                "lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias consequatur maxime quasi aliquid cum impedit rem. Sunt ipsa eius suscipit excepturi facilis, eum incidunt, itaque reprehenderit obcaecati eos maxime similique minus iusto explicabo rem, ex vitae possimus, tempore totam culpa aliquid fuga sapiente. Dolorum tempora mollitia ipsam laboriosam, esse commodi placeat tenetur reprehenderit alias sit laudantium magni maiores atque illo facere.",
                3.7F,
                "MajuJaya",
                "https://i.postimg.cc/dt7DmBT6/contoh2.jpg",
                "Patung",
            )
        )

        product.add(
            ProductModel(
                "Product4",
                "123400",
                "lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias consequatur maxime quasi aliquid cum impedit rem. Sunt ipsa eius suscipit excepturi facilis, eum incidunt, itaque reprehenderit obcaecati eos maxime similique minus iusto explicabo rem, ex vitae possimus, tempore totam culpa aliquid fuga sapiente. Dolorum tempora mollitia ipsam laboriosam, esse commodi placeat tenetur reprehenderit alias sit laudantium magni maiores atque illo facere.",
                4.7F,
                "Auuuu",
                "https://i.postimg.cc/6qFW1kG0/contoh3.jpg",
                "Patung",
            )
        )

        product.add(
            ProductModel(
                "Product5",
                "123400",
                "lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias consequatur maxime quasi aliquid cum impedit rem. Sunt ipsa eius suscipit excepturi facilis, eum incidunt, itaque reprehenderit obcaecati eos maxime similique minus iusto explicabo rem, ex vitae possimus, tempore totam culpa aliquid fuga sapiente. Dolorum tempora mollitia ipsam laboriosam, esse commodi placeat tenetur reprehenderit alias sit laudantium magni maiores atque illo facere.",
                2.7F,
                "AuShop",
                "https://i.postimg.cc/tT9x3Psf/1955455.webp",
                "Patung",
            )
        )

        product.add(
            ProductModel(
                "Product6",
                "123400",
                "lorem ipsum dolor sit amet, consectetur adipisicing elit. Alias consequatur maxime quasi aliquid cum impedit rem. Sunt ipsa eius suscipit excepturi facilis, eum incidunt, itaque reprehenderit obcaecati eos maxime similique minus iusto explicabo rem, ex vitae possimus, tempore totam culpa aliquid fuga sapiente. Dolorum tempora mollitia ipsam laboriosam, esse commodi placeat tenetur reprehenderit alias sit laudantium magni maiores atque illo facere.",
                1.7F,
                "ShopJkt",
                "https://i.postimg.cc/6qFW1kG0/contoh3.jpg",
                "Patung",
            )
        )

        return product
    }
}