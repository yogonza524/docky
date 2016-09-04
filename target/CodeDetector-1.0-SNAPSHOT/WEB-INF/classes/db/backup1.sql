--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.4
-- Dumped by pg_dump version 9.5.4

-- Started on 2016-09-04 17:26:07 ART

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 2225 (class 0 OID 16393)
-- Dependencies: 183
-- Data for Name: entry; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2224 (class 0 OID 16385)
-- Dependencies: 182
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: postgres
--



SET default_tablespace = '';

--
-- TOC entry 2106 (class 2606 OID 16400)
-- Name: entry_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entry
    ADD CONSTRAINT entry_pk PRIMARY KEY (id, id_project);


--
-- TOC entry 2104 (class 2606 OID 16392)
-- Name: project_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_pk PRIMARY KEY (id);


--
-- TOC entry 2108 (class 2620 OID 16445)
-- Name: before_insert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER before_insert BEFORE INSERT ON project FOR EACH ROW EXECUTE PROCEDURE before_insert_project();


--
-- TOC entry 2109 (class 2620 OID 16447)
-- Name: before_insert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER before_insert BEFORE INSERT ON entry FOR EACH ROW EXECUTE PROCEDURE before_insert_entry();


--
-- TOC entry 2107 (class 2606 OID 16401)
-- Name: entry_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entry
    ADD CONSTRAINT entry_fk FOREIGN KEY (id_project) REFERENCES project(id) ON DELETE CASCADE;


-- Completed on 2016-09-04 17:26:07 ART

--
-- PostgreSQL database dump complete
--

