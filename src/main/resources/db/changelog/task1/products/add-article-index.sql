--liquibase formatted sql

--changeset vassuv:2-idx_products_article
CREATE INDEX idx_products_article ON products(article);

--rollback DROP INDEX idx_products_article;