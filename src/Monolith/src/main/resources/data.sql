-- -----------------------------------------------------
-- Dealer data
-- -----------------------------------------------------
insert into ec_identity.dealer (id, name)
values (1, 'Shopocon'),
       (11, 'St Pete Distributing'),
       (12, 'Dunedin Distributing'),
       (13, 'Key West Distributors'),
       (21, 'PrimeNG');

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
values (1, 'Books'),
       (2, 'Beer'),
       (3, 'Clothing'),
       (4, 'Fitness'),
       (5, 'Accessories');

-- -----------------------------------------------------
-- Product data
-- -----------------------------------------------------
insert into ec_catalog.product(id, dealer_id, sku, category_id, name,
                               description, image, active,
                               units_in_stock, unit_price, rating, date_created)
values (1, 1, 'BOOK-TECH-1000', 1, 'JavaScript - The Fun Parts', 'Learn JavaScript',
        'placeholder.png', true, 100, 19.99, null, now()),
       (2, 1, 'BOOK-TECH-1001', 1, 'Spring Framework Tutorial', 'Learn Spring',
        null, true, 100, 29.99, null, now()),
       (3, 1, 'BOOK-TECH-1002', 1, 'Kubernetes - Deploying Containers', 'Learn Kubernetes',
        'placeholder.png', true, 100, 24.99, null, now()),
       (4, 1, 'BOOK-TECH-1003', 1, 'Internet of Things (IoT) - Getting Started', 'Learn IoT',
        'placeholder.png', true, 100, 29.99, null, now()),
       (5, 1, 'BOOK-TECH-1004', 1, 'The Go Programming Language: A to Z', 'Learn Go',
        'placeholder.png', true, 100, 24.99, null, now()),
       -- Spring Guru
       (6, 11, '0631234200036', 2, 'Mango Bobs', 'IPA', 'placeholder.png', true, 50, 1.99, null, now()),
       (7, 12, '0631234300019', 2, 'Galaxy Cat', 'PALE ALE', 'placeholder.png', true, 70, 1.59, null, now()),
       (8, 13, '0083783375213', 2, 'Pinball Porter', 'PORTER', 'placeholder.png', true, 30, 2.19, null, now()),
       -- PrimeNG
       (1002, 21, 'zz21cz3c1', 4, 'Blue Band', 'Product Description', 'blue-band.jpg', true, 2, 79.00, 60, now()),
       (1003, 21, '244wgerg2', 3, 'Blue T-Shirt', null, 'blue-t-shirt.jpg', true, 25, 29.00, 100, now()),
       (1004, 21, 'h456wer53', 5, 'Bracelet', 'Product Description', 'bracelet.jpg', true, 73, 15.00, 80, now()),
       (1006, 21, 'bib36pfvm', 5, 'Chakra Bracelet', 'Product Description',
        'chakra-bracelet.jpg', true, 5, 32.00, 60, now()),
       (1012, 21, '250vm23cc', 3, 'Green T-Shirt', 'Product Description',
        'green-t-shirt.jpg', true, 74, 49.00, 100, now()),
       (1013, 21, 'fldsmn31bt', 3, 'Grey T-Shirt', 'Product Description',
        'grey-t-shirt.jpg', true, 0, 48.00, 60, now()),
       (1015, 21, 'vb34btbg5', 3, 'Light Green T-Shirt', 'Product Description',
        'light-green-t-shirt.jpg', true, 34, 49.00, 80, now()),
       (1016, 21, 'k8l6j58jl', 4, 'Lime Band', 'Product Description', 'lime-band.jpg', true, 12, 79.00, 60, now()),
       (1019, 21, 'mnb5mb2m5', 4, 'Pink Band', 'Product Description', 'pink-band.jpg', true, 63, 79.00, 80, now()),
       (1021, 21, 'pxpzczo23', 4, 'Purple Band', 'Product Description', 'purple-band.jpg', true, 6, 79.00, 60, now()),
       (1022, 21, '2c42cb5cb', 5, 'Purple Gemstone Necklace', 'Product Description',
        'purple-gemstone-necklace.jpg', true, 62, 45.00, 80, now()),
       (1023, 21, '5k43kkk23', 3, 'Purple T-Shirt', 'Product Description',
        'purple-t-shirt.jpg', true, 2, 49.00, 100, now()),
       (1024, 21, 'lm2tny2k4', 3, 'Shoes', 'Product Description', 'shoes.jpg', true, 0, 64.00, 80, now()),
       (1025, 21, 'nbm5mv45n', 3, 'Sneakers', 'Product Description', 'sneakers.jpg', true, 52, 78.00, 90, now()),
       (1026, 21, 'zx23zc42c', 3, 'Teal T-Shirt', 'Product Description', 'teal-t-shirt.jpg', true, 3, 49.00, 60, now()),
       (1028, 21, 'tx125ck42', 4, 'Yoga Mat', 'Product Description', 'yoga-mat.jpg', true, 15, 19.99, 100, now()),
       (1029, 21, 'gwuby345v', 4, 'Yoga Set', 'Product Description', 'yoga-set.jpg', true, 25, 19.99, 80, now());

-- -----------------------------------------------------
-- Product - review data
-- -----------------------------------------------------
insert into ec_catalog.product_review(id, product_id, user_id, user_alias, text, date_created)
values (1, 2, 203, 'Hidden', 'Awesome book', now()),
       (2, 7, 202, 'Hidden', 'Meow', now()),
       (3, 7, 203, 'Hidden', 'Excellent beer', now() + 1);
