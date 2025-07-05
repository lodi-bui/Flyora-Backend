package org.example.flyora_backend.repository;

import jakarta.persistence.*;

import org.example.flyora_backend.DTOs.OwnerProductListDTO;
import org.example.flyora_backend.DTOs.ProductBestSellerDTO;
import org.example.flyora_backend.DTOs.ProductListDTO;
import org.example.flyora_backend.DTOs.TopProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<ProductListDTO> filterProducts(String name, Integer categoryId, Integer birdTypeId, BigDecimal minPrice,
            BigDecimal maxPrice, Pageable pageable) {
        String sql = """
                    SELECT p.id, p.name, c.name AS category, bt.name AS birdType,
                        p.price, p.stock,
                        COALESCE(fd.image_url, td.image_url, fud.image_url) AS imageUrl
                    FROM Product p
                    JOIN ProductCategory c ON p.category_id = c.id
                    JOIN BirdType bt ON p.bird_type_id = bt.id
                    LEFT JOIN FoodDetail fd ON p.id = fd.product_id AND c.name = 'FOODS'
                    LEFT JOIN ToyDetail td ON p.id = td.product_id AND c.name = 'TOYS'
                    LEFT JOIN FurnitureDetail fud ON p.id = fud.product_id AND c.name = 'FURNITURE'
                    WHERE (:name IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%'))
                    AND (:categoryId IS NULL OR p.category_id = :categoryId)
                    AND (:birdTypeId IS NULL OR p.bird_type_id = :birdTypeId)
                    AND (:minPrice IS NULL OR p.price >= :minPrice)
                    AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                    ORDER BY p.id
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("name", name != null ? name : null);
        query.setParameter("categoryId", categoryId != null ? categoryId : null);
        query.setParameter("birdTypeId", birdTypeId != null ? birdTypeId : null);
        query.setParameter("minPrice", minPrice != null ? minPrice : null);
        query.setParameter("maxPrice", maxPrice != null ? maxPrice : null);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<ProductListDTO> dtos = new ArrayList<>();
        for (Object[] row : rows) {
            ProductListDTO dto = new ProductListDTO();
            dto.setId((Integer) row[0]);
            dto.setName((String) row[1]);
            dto.setCategory((String) row[2]);
            dto.setBirdType((String) row[3]);
            dto.setPrice((BigDecimal) row[4]);
            dto.setStock((Integer) row[5]);
            dto.setImageUrl((String) row[6]);
            dtos.add(dto);
        }

        String countSql = """
                    SELECT COUNT(*)
                    FROM Product p
                    JOIN ProductCategory c ON p.category_id = c.id
                    WHERE (:name IS NULL OR LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%'))
                    AND (:categoryId IS NULL OR p.category_id = :categoryId)
                    AND (:birdTypeId IS NULL OR p.bird_type_id = :birdTypeId)
                    AND (:minPrice IS NULL OR p.price >= :minPrice)
                    AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                """;

        Query countQuery = em.createNativeQuery(countSql);
        countQuery.setParameter("name", name);
        countQuery.setParameter("categoryId", categoryId);
        countQuery.setParameter("birdTypeId", birdTypeId);
        countQuery.setParameter("minPrice", minPrice);
        countQuery.setParameter("maxPrice", maxPrice);

        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(dtos, pageable, total);
    }

    @Override
    public List<ProductBestSellerDTO> findBestSellersTop1PerCategory() {
        String sql = """
                    SELECT p.id AS productId, p.name AS productName, c.name AS categoryName,
                        p.price AS price, SUM(oi.quantity) AS totalSold,
                        COALESCE(fd.image_url, td.image_url, fud.image_url) AS imageUrl
                    FROM OrderItem oi
                    JOIN Product p ON oi.product_id = p.id
                    JOIN ProductCategory c ON p.category_id = c.id
                    LEFT JOIN FoodDetail fd ON p.id = fd.product_id AND c.name = 'FOODS'
                    LEFT JOIN ToyDetail td ON p.id = td.product_id AND c.name = 'TOYS'
                    LEFT JOIN FurnitureDetail fud ON p.id = fud.product_id AND c.name = 'FURNITURE'
                    GROUP BY p.id, p.name, c.name, p.price, imageUrl
                """;

        Query query = em.createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        // Map để giữ top 1 sản phẩm mỗi category
        Map<String, ProductBestSellerDTO> bestSellers = new HashMap<>();

        for (Object[] row : results) {
            Integer productId = (Integer) row[0];
            String name = (String) row[1];
            String category = (String) row[2];
            BigDecimal price = (BigDecimal) row[3];
            Long totalSold = ((Number) row[4]).longValue();
            String imageUrl = (String) row[5];

            ProductBestSellerDTO dto = new ProductBestSellerDTO(productId, name, category, price, totalSold, imageUrl);

            // Giữ lại sản phẩm có lượng bán cao nhất cho mỗi category
            if (!bestSellers.containsKey(category) || bestSellers.get(category).totalSold() < totalSold) {
                bestSellers.put(category, dto);
            }
        }

        return new ArrayList<>(bestSellers.values());
    }

    @Override
    public List<ProductListDTO> searchByName(String name) {
        String sql = """
                    SELECT p.id, p.name, c.name AS category, bt.name AS birdType,
                        p.price, p.stock,
                        COALESCE(fd.image_url, td.image_url, fud.image_url) AS imageUrl
                    FROM Product p
                    JOIN ProductCategory c ON p.category_id = c.id
                    JOIN BirdType bt ON p.bird_type_id = bt.id
                    LEFT JOIN FoodDetail fd ON p.id = fd.product_id AND c.name = 'FOODS'
                    LEFT JOIN ToyDetail td ON p.id = td.product_id AND c.name = 'TOYS'
                    LEFT JOIN FurnitureDetail fud ON p.id = fud.product_id AND c.name = 'FURNITURE'
                    WHERE LOWER(p.name) LIKE CONCAT('%', LOWER(:name), '%')
                    ORDER BY p.name ASC
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("name", name);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<ProductListDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            ProductListDTO dto = new ProductListDTO();
            dto.setId((Integer) row[0]);
            dto.setName((String) row[1]);
            dto.setCategory((String) row[2]);
            dto.setBirdType((String) row[3]);
            dto.setPrice((BigDecimal) row[4]);
            dto.setStock((Integer) row[5]);
            dto.setImageUrl((String) row[6]);
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<TopProductDTO> findTopSellingProductsByShopOwner(int shopOwnerId) {
        String sql = """
                    SELECT p.id, p.name, p.price,
                        SUM(oi.quantity) AS totalSold,
                        COALESCE(fd.image_url, td.image_url, fud.image_url) AS imageUrl
                    FROM Product p
                    JOIN ProductCategory c ON p.category_id = c.id
                    LEFT JOIN FoodDetail fd ON p.id = fd.product_id AND c.name = 'FOODS'
                    LEFT JOIN ToyDetail td ON p.id = td.product_id AND c.name = 'TOYS'
                    LEFT JOIN FurnitureDetail fud ON p.id = fud.product_id AND c.name = 'FURNITURE'
                    LEFT JOIN OrderItem oi ON p.id = oi.product_id
                    WHERE p.shop_owner_id = :shopOwnerId
                    GROUP BY p.id, p.name, p.price, imageUrl
                    ORDER BY totalSold DESC
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("shopOwnerId", shopOwnerId);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<TopProductDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            Integer productId = (Integer) row[0];
            String name = (String) row[1];
            BigDecimal price = (BigDecimal) row[2];
            Long totalSold = row[3] != null ? ((Number) row[3]).longValue() : 0L;
            String imageUrl = (String) row[4];

            TopProductDTO dto = new TopProductDTO(
                    productId.longValue(),
                    name,
                    imageUrl,
                    totalSold.intValue(),
                    price);

            result.add(dto);
        }

        return result;
    }

    @Override
    public List<OwnerProductListDTO> findAllByShopOwnerIdOrderByIdAsc(int ownerId) {
        String sql = """
                    SELECT p.id, p.name, p.price, p.stock,
                           COALESCE(fd.image_url, td.image_url, fud.image_url) AS imageUrl
                    FROM Product p
                    JOIN ProductCategory c ON p.category_id = c.id
                    LEFT JOIN FoodDetail fd ON p.id = fd.product_id AND c.name = 'FOODS'
                    LEFT JOIN ToyDetail td ON p.id = td.product_id AND c.name = 'TOYS'
                    LEFT JOIN FurnitureDetail fud ON p.id = fud.product_id AND c.name = 'FURNITURE'
                    WHERE p.shop_owner_id = :ownerId
                    ORDER BY p.id ASC
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("ownerId", ownerId);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<OwnerProductListDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            Integer id = (Integer) row[0];
            String name = (String) row[1];
            BigDecimal price = (BigDecimal) row[2];
            Integer stock = (Integer) row[3];
            String imageUrl = (String) row[4];

            String status = (stock == null || stock <= 0) ? "Hết hàng" : "Còn hàng";

            result.add(new OwnerProductListDTO(id, name, price, stock, status, imageUrl));
        }

        return result;
    }

}
