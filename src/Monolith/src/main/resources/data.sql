-- -----------------------------------------------------
-- Dealer data
-- -----------------------------------------------------
insert into ec_identity.dealer (id, name)
values (1, 'Shopocon'),
       (11, 'St Pete Distributing'),
       (12, 'Dunedin Distributing'),
       (13, 'Key West Distributors');

-- -----------------------------------------------------
-- User data
-- -----------------------------------------------------
insert into ec_identity.user (id, username, password,
                              account_non_expired,
                              account_non_locked,
                              credentials_non_expired,
                              enabled,
                              dealer_representative,
                              dealer_id)
values (1, 'shopocon1', 'shopocon', true, true, true, true, true, 1),
       (2, 'shopocon2', 'eShopOnContainer', true, true, true, true, true, 1),
       (101, 'stpete', 'password', true, true, true, true, true, 11),
       (102, 'dunedin', 'password', true, true, true, true, true, 12),
       (103, 'keywest', 'password', true, true, true, true, true, 13),
       (201, 'scott', 'tiger', true, true, true, true, false, null),
       (202, 'user', 'password', true, true, true, true, false, null),
       (203, 'buyer', 'shopper', true, true, true, true, false, null);

-- -----------------------------------------------------
-- Authority data
-- -----------------------------------------------------
insert into ec_identity.authority(id, permission)
values -- user
       (1, 'user.create'),
       (2, 'user.read'),
       (3, 'user.update'),
       (4, 'user.delete'),
       -- dealer
       (5, 'dealer.create'),
       (6, 'dealer.read'),
       (7, 'dealer.update'),
       (8, 'dealer.delete'),
       -- product
       (10, 'product.create'),
       (11, 'product.read'),
       (12, 'product.update'),
       (13, 'product.delete'),
       -- dealer product
       (14, 'dealer.product.create'),
       (15, 'dealer.product.read'),
       (16, 'dealer.product.update'),
       (17, 'dealer.product.delete');

-- -----------------------------------------------------
-- Role data
-- -----------------------------------------------------
insert into ec_identity.role (id, name)
values (1, 'ADMIN'),
       (2, 'DEALER'),
       (3, 'USER');

-- -----------------------------------------------------
-- Role-authority data
-- -----------------------------------------------------
insert into ec_identity.role_authority(id, role_id, authority_id)
values -- admin - user authority
       (1, 1, 1),
       (2, 1, 2),
       (3, 1, 3),
       (4, 1, 4),
       -- admin - dealer authority
       (5, 1, 5),
       (6, 1, 6),
       (7, 1, 7),
       (8, 1, 8),
       -- admin - product authority
       (10, 1, 10),
       (11, 1, 11),
       (12, 1, 12),
       (13, 1, 13),
       -- dealer - dealer.product authority
       (14, 2, 14),
       (15, 2, 15),
       (16, 2, 16),
       (17, 2, 17),
       -- user - product read only
       (18, 3, 11);

-- -----------------------------------------------------
-- User-role data
-- -----------------------------------------------------
insert into ec_identity.user_role(id, user_id, role_id)
values (1, 1, 1),
       (2, 2, 2),
       (3, 101, 2),
       (4, 102, 2),
       (5, 103, 2),
       (6, 201, 1),
       (7, 202, 3),
       (8, 203, 3);

-- -----------------------------------------------------
-- Product-category data
-- -----------------------------------------------------
insert into ec_catalog.product_category(id, category_name)
values (1, 'BOOKS'),
       (2, 'BEER');

-- -----------------------------------------------------
-- Product data
-- -----------------------------------------------------
insert into ec_catalog.product(id, dealer_id, sku, category_id, name,
                               description, image_url, active,
                               units_in_stock, unit_price, date_created)
values (1, 1, 'BOOK-TECH-1000', 1, 'JavaScript - The Fun Parts', 'Learn JavaScript',
        'assets/images/products/placeholder.png', 1, 100, 19.99, now()),
       (2, 1, 'BOOK-TECH-1001', 1, 'Spring Framework Tutorial', 'Learn Spring',
        'assets/images/products/placeholder.png', 1, 100, 29.99, now()),
       (3, 1, 'BOOK-TECH-1002', 1, 'Kubernetes - Deploying Containers', 'Learn Kubernetes',
        'assets/images/products/placeholder.png', 1, 100, 24.99, now()),
       (4, 1, 'BOOK-TECH-1003', 1, 'Internet of Things (IoT) - Getting Started', 'Learn IoT',
        'assets/images/products/placeholder.png', 1, 100, 29.99, now()),
       (5, 1, 'BOOK-TECH-1004', 1, 'The Go Programming Language: A to Z', 'Learn Go',
        'assets/images/products/placeholder.png', 1, 100, 24.99, now()),
       (6, 11, '0631234200036', 2, 'Mango Bobs', 'IPA',
        'assets/images/products/placeholder.png', 1, 50, 1.99, now()),
       (7, 12, '0631234300019', 2, 'Galaxy Cat', 'PALE ALE',
        'assets/images/products/placeholder.png', 1, 70, 1.59, now()),
       (8, 13, '0083783375213', 2, 'Pinball Porter', 'PORTER',
        'assets/images/products/placeholder.png', 1, 30, 2.19, now());

-- -----------------------------------------------------
-- Product - review data
-- -----------------------------------------------------
insert into ec_catalog.product_review(id, product_id, user_id, user_alias, text, date_created)
values (1, 2, 203, 'Hidden', 'Awesome book', now()),
       (2, 7, 202, 'Hidden', 'Meow', now()),
       (3, 7, 203, 'Hidden', 'Excellent beer', now() + 1);
