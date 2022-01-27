DELIMITER $$$
CREATE FUNCTION `ufn_get_salary_level`(`salary_of_employee` DECIMAL)
RETURNS VARCHAR(10)
DETERMINISTIC
BEGIN
	RETURN (CASE
				WHEN `salary_of_employee` < 30000 THEN 'Low'
				WHEN `salary_of_employee` BETWEEN 30000 AND 50000 THEN 'Average'
				WHEN `salary_of_employee` > 50000 THEN 'High'
			END
	);
END; $$$
DELIMITER ;

DELIMITER $$$
CREATE PROCEDURE `usp_get_employees_by_salary_level`(`level_of_salary` VARCHAR(10))
BEGIN
	SELECT `first_name`, `last_name`
    FROM `employees`
    WHERE `ufn_get_salary_level`(`salary`) = `level_of_salary`
    ORDER BY `first_name` DESC, `last_name` DESC;
END; $$$
DELIMITER ;

CALL `usp_get_employees_by_salary_level`('high');