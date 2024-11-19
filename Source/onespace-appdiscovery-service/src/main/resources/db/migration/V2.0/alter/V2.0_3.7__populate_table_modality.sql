BEGIN;
DO $$
BEGIN
    INSERT INTO modality (modality_name, description)
VALUES ('X-ray (Radiography)','Used to view bones and detect fractures, infections, or tumors.'),
       ('Ultrasound (Sonography)', 'Utilizes sound waves to produce images, commonly used in obstetrics and for soft tissues.'),
       ('Magnetic Resonance Imaging (MRI)', 'Provides detailed images of organs and tissues using magnetic fields and radio waves.'),
       ('Computed Tomography (CT)','Combines X-rays with computer processing to create cross-sectional images of the body.'),
       ('Positron Emission Tomography (PET)','Often combined with CT or MRI, used to visualize metabolic processes in the body, especially for cancer.'),
       ('Mammography','Specialized X-ray for breast tissue, used for early detection of breast cancer.'),
       ('Fluoroscopy','Continuous X-ray imaging used for guiding procedures like catheter insertions.'),
       ('Nuclear Medicine','Uses small amounts of radioactive material to diagnose or treat diseases.'),
       ('Echocardiography','A type of ultrasound specifically for imaging the heart.'),
       ('ECG','method of measuring and displaying electrical activity of the heart'),
       ('MRI','noninvasive medical test that provides images of the soft tissues'),
       ('Digital Pathology','focuses on managing and analyzing information generated from digitized specimen slides.'),
       ('Xray','Used to to create images of structures inside the body'),
       ('Nuclear Medicine','uses very small amounts of a radioactive substance (radionuclide or radio-tracer) for health research, diagnosis, and treatment of various conditions, including cancer.');

    UPDATE modality SET description = 'Not applicable' WHERE modality_name = 'Not applicable';

    delete from modality  where modality_name = 'CT';

    delete from modality  where modality_name = 'MR';

    delete from modality  where modality_name = 'Ultrasound';


    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;