BEGIN;
DO $$
BEGIN

update speciality set description = 'Involves pain management and anesthesia during surgery or procedures.' where speciality_name = 'Anesthesiology';

update speciality set description = 'Treats disorders of the heart and blood vessels.' where speciality_name = 'Cardiology';

update speciality set description = 'Focuses on skin, hair, and nail conditions.' where speciality_name = 'Dermatology';

update speciality set description = 'Provides care for urgent, acute illnesses and injuries.' where speciality_name = 'Emergency Medicine';

update speciality set description = 'Treats hormone-related conditions, including diabetes and thyroid disorders.' where speciality_name = 'Endocrinology';

update speciality set description = 'Treats blood disorders.' where speciality_name = 'Hematology';

update speciality set description = 'Treats kidney conditions.' where speciality_name = 'Nephrology';

update speciality set description = 'Treats cancer and tumors.' where speciality_name = 'Oncology';

update speciality set description = 'Focuses on eye conditions and surgery.' where speciality_name = 'Ophthalmology';

update speciality set description = 'Treats musculoskeletal disorders, including bones, joints, and muscles.' where speciality_name = 'Orthopedics';

update speciality set description = 'Specializes in medical care for infants, children, and adolescents.' where speciality_name = 'Pediatrics';

update speciality set description = 'Focuses on lung and respiratory conditions.' where speciality_name = 'Pulmonology';

update speciality set description = 'Uses imaging techniques (e.g., X-rays, MRI) for diagnosis and treatment.' where speciality_name = 'Radiology';

update speciality set description = 'General surgery or specialized fields like cardiothoracic or neurosurgery.' where speciality_name = 'Surgery';

update speciality set description = 'Focuses on urinary system disorders and male reproductive health.' where speciality_name = 'Urology';

update speciality set speciality_name = 'Obstetrics and Gynecology (OB/GYN)' where speciality_name = 'Obstetrics and Gynecology';

update speciality set description = 'Specializes in pregnancy, childbirth, and female reproductive health.' where speciality_name = 'Obstetrics and Gynecology (OB/GYN)';

update speciality set description = 'Not applicable' where speciality_name = 'Not applicable';




INSERT INTO speciality (speciality_name, description)
VALUES
('Gastroenterology', 'Focuses on the digestive system and its disorders.'),
('Geriatrics', 'Specializes in care for the elderly.'),
('Infectious Disease', 'Focuses on infections and disease-causing pathogens.'),
('Internal Medicine', 'General adult medicine focusing on the prevention, diagnosis, and treatment of adult diseases.'),
('Otolaryngology (ENT)', 'Focuses on ear, nose, and throat disorders.'),
('Pathology', 'Studies disease through lab tests and examination of tissues.'),
('Plastic Surgery', 'Involves reconstructive and cosmetic surgical procedures.'),
('Psychiatry', 'Treats mental health disorders.'),
('Rheumatology', 'Treats autoimmune and musculoskeletal conditions, such as arthritis.');

    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;