package org.example.flyora_backend.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import org.example.flyora_backend.DTOs.ProductBestSellerDTO;
import org.example.flyora_backend.DTOs.ProductListDTO;
import org.example.flyora_backend.model.Product;
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
    public Page<ProductListDTO> filterProducts(String name, Integer categoryId, Integer birdTypeId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductListDTO> cq = cb.createQuery(ProductListDTO.class);
        Root<Product> root = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (categoryId != null) {
            predicates.add(cb.equal(root.get("category").get("id"), categoryId));
        }
        if (birdTypeId != null) {
            predicates.add(cb.equal(root.get("birdType").get("id"), birdTypeId));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        cq.select(cb.construct(
                ProductListDTO.class,
                root.get("id"),
                root.get("name"),
                root.get("category").get("name"),
                root.get("birdType").get("name"),
                root.get("price"),
                root.get("stock")
        )).where(predicates.toArray(new Predicate[0]));

        TypedQuery<ProductListDTO> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);
        List<Predicate> countPredicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            countPredicates.add(cb.like(cb.lower(countRoot.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (categoryId != null) {
            countPredicates.add(cb.equal(countRoot.get("category").get("id"), categoryId));
        }
        if (birdTypeId != null) {
            countPredicates.add(cb.equal(countRoot.get("birdType").get("id"), birdTypeId));
        }
        if (minPrice != null) {
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("price"), minPrice));
        }
        if (maxPrice != null) {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("price"), maxPrice));
        }

        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
        Long count = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, count);
    }

    @Override
    public List<ProductBestSellerDTO> findBestSellersTop1PerCategory() {
        String sql = """
            SELECT p.id AS productId, p.name AS productName, c.name AS categoryName, 
                p.price AS price, SUM(oi.quantity) AS totalSold
            FROM OrderItem oi
            JOIN Product p ON oi.product_id = p.id
            JOIN ProductCategory c ON p.category_id = c.id
            GROUP BY p.id, p.name, c.name, p.price
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

            ProductBestSellerDTO dto = new ProductBestSellerDTO(productId, name, category, price, totalSold);

            // Giữ lại sản phẩm có lượng bán cao nhất cho mỗi category
            if (!bestSellers.containsKey(category) || bestSellers.get(category).totalSold() < totalSold) {
                bestSellers.put(category, dto);
            }
        }

        return new ArrayList<>(bestSellers.values());
    }
}
