USE `book_library`;

SELECT 
    `title`
FROM
    `books`
WHERE
    `title` LIKE 'The%'
ORDER BY `id` ASC;