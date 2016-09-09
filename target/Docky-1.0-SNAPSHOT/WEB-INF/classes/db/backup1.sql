--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.14
-- Dumped by pg_dump version 9.3.14
-- Started on 2016-09-05 13:19:46 ART

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 1 (class 3079 OID 11829)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2065 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 2 (class 3079 OID 16406)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- TOC entry 2066 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET search_path = public, pg_catalog;

--
-- TOC entry 221 (class 1255 OID 16443)
-- Name: before_insert_entry(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION before_insert_entry() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
	exists_project boolean;
BEGIN
	NEW.id = uuid();
	NEW.date = now();
	IF (NEW.id_project IS NULL) THEN
		RAISE EXCEPTION 'The id of project is mandatory';
	ELSE
		SELECT COUNT(*)>0 INTO exists_project FROM project p WHERE p.id = NEW.id_project;
		IF (NOT exists_project) THEN
			RAISE EXCEPTION 'ID of project not found';
		END IF;
	END IF;
	IF (NEW.title IS NULL) THEN
		RAISE EXCEPTION 'The title is mandatory';
	END IF;
	RETURN NEW;
END;
$$;


ALTER FUNCTION public.before_insert_entry() OWNER TO postgres;

--
-- TOC entry 220 (class 1255 OID 16441)
-- Name: before_insert_project(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION before_insert_project() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE

BEGIN
	NEW.id = uuid();
	IF (NEW.name IS NULL) THEN
		RAISE EXCEPTION 'The name is mandatory';
	END IF;
	RETURN NEW;
END;
$$;


ALTER FUNCTION public.before_insert_project() OWNER TO postgres;

--
-- TOC entry 219 (class 1255 OID 16440)
-- Name: uuid(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION uuid() RETURNS character varying
    LANGUAGE plpgsql
    AS $$
DECLARE
	output character varying(255);
BEGIN
	SELECT md5(random()::text || clock_timestamp()::text)::uuid INTO output;
	output = replace(output,'-','');
	RETURN output;
END;
$$;


ALTER FUNCTION public.uuid() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 173 (class 1259 OID 16393)
-- Name: entry; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE entry (
    id character varying(64) NOT NULL,
    id_project character varying(64) NOT NULL,
    content text,
    title character varying(512),
    date timestamp with time zone
);


ALTER TABLE public.entry OWNER TO postgres;

--
-- TOC entry 172 (class 1259 OID 16385)
-- Name: project; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE project (
    id character varying(64) NOT NULL,
    name character varying(128) NOT NULL,
    description character varying(1024)
);


ALTER TABLE public.project OWNER TO postgres;

--
-- TOC entry 2057 (class 0 OID 16393)
-- Dependencies: 173
-- Data for Name: entry; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO entry (id, id_project, content, title, date) VALUES ('dd5901012c079772e2134a17a5c937e9', '20010c461a7070f44937247b7acff14a', '
                            <br>
                                <h1>El problema</h1>
                            <br>
                                <p>Investigando en el foro de New life Games se encontró que muchas personas tenian problemas con la comunicacion entre el EGM y el Host y viceversa. El protocolo establece un formato de trama de 11 bits: 1 bit de inicio, 8 bits de datos, 1 bit llamado ''wake up bit'' y 1 bit de parada</p>
                            <br>
                                <p>Cuando se quiere implementar la comunicacion entre el EGM y el Host el problema surge con el wake up bit, este bit indica el comienzo de cada paquete y los sistemas operativos como Windows y Linux no pueden interpretarlo.</p>
                            <br>
                                <p>Teniendo en cuenta que utilizamos una distribución Linux Ubuntu 14, cuando se intenta leer los datos del puerto serie (RS232) los paquetes llegan cortados o en formatos distintos a los esperados. Esto se debe a que el wakeup bit nunca esta configurado correctamente. Esto inhabilita cualquier comunicación con el host (online) ya que los extremos de la comunicación no entienden el código del canal. Por otro lado es vital que se pueda manipular correctamente el wakeup bit ya que sino queda excluida cualquier posibilidad de certificación de la máquina construida.</p>
                            <br>
                                <p>Implementar una solución de hardware que realice la interpretación de los bits, arme los paquetes y se los envie a la EGM. Otros han implementado interfaces Arduino UNO o Raspberry Pi 2 o similar.</p>
                            <br>
                                <p>Implementar una solución de hardware que realice la interpretación de los bits, arme los paquetes y se los envie a la EGM. Otros han implementado interfaces Arduino UNO o Raspberry Pi 2 o similar.

Existen soluciones de software por el cual se emula el comportamiento del bit de paridad MARK y SPACE, por ejemplo este autor muestra como, pero no es factible implementar ya que los tiempos de respuesta del protocolo exigen un tiempo de 5 ms para contestar el mensaje. Cualquier implementación que manipule estos bits a alto nivel llevara un tiempo de 16, 40 o mas milisegundos para hacer la conversión, empaquetado y envío. Verificar los siguientes enlaces al respecto </p>
                            <br>
                                <a href=''https://viereck.ch/linux-mark-space-parity/''>https://viereck.ch/linux-mark-space-parity/</a>
                            <br>
                                <a href=''https://viereck.ch/linux-mark-space-parity/''>https://viereck.ch/linux-mark-space-parity/</a>
                    ', 'Problema con el Wake Up Bit', '2016-09-05 11:17:15.757657-03');
INSERT INTO entry (id, id_project, content, title, date) VALUES ('a057803e0e9a181b2d20991d6db8dc89', '20010c461a7070f44937247b7acff14a', '
                            <br>
                                <h1>Heejune Ahn:</h1>
                            <br>
                                <p>Wakeup bit in SAS was one of difficulty in implementaton to me.

As you know, SAS use wakeup bit for  ''Start of Frame'' from Host only, because of master and slave nature of the protocol.  In 
In implementation aspect, wakeup bit requires  ''9 bit data''  serial communication, which is not supported in most modern computer (they are 8 bit oriented).

One typical way of emulating 9 bit communication is ''emulating 9 bits communication using parity bit''.  For machine side, you only need to detect the MARK of Parity bit, and all other data bytes is essentially  SPACE parity.

So,  you can set up the Linux serial''s terminal type as ''SPACE parity mode,''  and detect the Parity error when Host send the Mark bit.  The detail  way can be found easily here:  http://www.tldp.org/HOWTO/Serial-ProgrammingHOWTO/ 

For more information, you had better inform me of more details of your project and identy.
As a professor, I olnly work for patents and publications, and payed consulting.</p>
                            <br>
                                <h1>Me:</h1>
                            <br>
                                <p>The link you shared to me is broken. Anyway, I was reading about parity bits in POSIX systems and as I see the SPACE parity and MARK parity can''t be detected by that system.

My implementation consist in a Java module connected to the game through sockets connections (ActionScript game) runing in Linux Ubuntu 14. The Java module implements SAS 6.02 logic interface.

The solutions that I found only ''emule'' the behaviour of 9th bit and that has an expensive cost for the machine in execution time. AS the protocol says: it needs 40 ms for polling rate communication and the emulation greater time than thats 40 ms.

Do you recommend me implements a driver in low level programming or make a hardware interface for manipulate all frames incoming from the host?</p>
                            <br>
                                <h1>Heejune Ahn</h1>
                            <br>
                                <p>Sorry for the broken link.  But you can find it by googling ''linux how to programming serial''. The api you have to use is ''http://linux.die.net/man/3/tcsetattr''

Yes, I  found ath some emedded Linux systems serial driver doesnot support  space and mark parity, and they only support odd and even.  I used many HW systems and one of them (maybe freescale-based board)  didnot support.  But, as I know, Ubuntu OS based one does support space and mark.   So, you had better to check if your system support it or not.   Apply the following. 

PARMRK

If IGNPAR is not set, prefix a character with a parity error or framing error with \377 \0. If neither IGNPAR nor PARMRK is set, read a character with a parity error or framing error as \0.



If not, yes, you can implement your own simple driver for uart device, or  modify the original serial driver to support SPACE parity, or use another system that handle the communication.  It depend upon the condition which one is better.  In my personal tastes, I recommend you do the first option. 

But, the 3 options all needs customization of your system, which is not pssoible, when you want to run your software in any Linux compatible system. So, I recommend you check if your system support space parity or not.   You can test with SAS Tester. </p>
                    ', 'Charla con el Dr. Heejune Ahn sobre el protocolo SAS', '2016-09-05 12:05:42.623042-03');
INSERT INTO entry (id, id_project, content, title, date) VALUES ('e888edda2bb46a1624cf92c7ec2f0174', '20010c461a7070f44937247b7acff14a', '
                            <br>
                                <a href=''http://onlinepresent.org/proceedings/vol54_2014/17.pdf''>http://onlinepresent.org/proceedings/vol54_2014/17.pdf</a>
                    ', 'Link del Paper coreano sobre el protocolo SAS', '2016-09-05 12:34:05.441883-03');
INSERT INTO entry (id, id_project, content, title, date) VALUES ('41712cbb40d8ec40f4fe788ce5bdd4ad', '20010c461a7070f44937247b7acff14a', '
                            <br>
                                <a href=''http://newlifegames.net/nlg/index.php?board=105.0''>http://newlifegames.net/nlg/index.php?board=105.0</a>
                            <br>
                                <p>No deja registrarse, hay que ingresar en el nuevo foro</p>
                    ', 'Link del foro New Life Games', '2016-09-05 13:11:44.958796-03');
INSERT INTO entry (id, id_project, content, title, date) VALUES ('e0f04e34df9faa3b8acf153ecf347901', '20010c461a7070f44937247b7acff14a', '
                            <br>
                                <p>El CRC aplicado aquí sigue la convención básica del CCITT (Comité Consultivo Internacional Telegráfico y Telefónico) comenzando con el byte mas significativo, menos significativo y aplicando el polinomio CRC </p>
                            <br>
                                <p>El procedimiento puede ser utilizado para generar el CRC de todos los mensajes del protocolo como también el cálculo de la semilla variable (variable-seed) necesario para calcular la firma ROM o ROM Signature. </p>
                            <br>
                                <pre><code class=''java''>public static byte[] crc(byte[] val)
{
    long crc;
    long q;
    byte c;
    crc = 0;
    for (int i = 0; i &lt; val.length; i++)
    {
        c = val[i];
        q = (crc ^ c) &amp; 0x0f;
        crc = (crc &gt;&gt; 4) ^ (q * 0x1081);
        q = (crc ^ (c &gt;&gt; 4)) &amp; 0xf;
        crc = (crc &gt;&gt; 4) ^ (q * 0x1081);
    }
    return new byte[] { (byte)(crc &amp; 0xff),(byte)(crc &gt;&gt; 8) };
}</code></pre>
                    ', 'CRC en Java', '2016-09-05 13:12:44.652047-03');


--
-- TOC entry 2056 (class 0 OID 16385)
-- Dependencies: 172
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO project (id, name, description) VALUES ('20010c461a7070f44937247b7acff14a', 'SAS Protocol', 'Projecto de software capaz de realizar la comunicacion entre el EGM y el HOST');


--
-- TOC entry 1945 (class 2606 OID 16400)
-- Name: entry_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY entry
    ADD CONSTRAINT entry_pk PRIMARY KEY (id, id_project);


--
-- TOC entry 1943 (class 2606 OID 16392)
-- Name: project_pk; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_pk PRIMARY KEY (id);


--
-- TOC entry 1947 (class 2620 OID 16442)
-- Name: before_insert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER before_insert BEFORE INSERT ON project FOR EACH ROW EXECUTE PROCEDURE before_insert_project();


--
-- TOC entry 1948 (class 2620 OID 16444)
-- Name: before_insert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER before_insert BEFORE INSERT ON entry FOR EACH ROW EXECUTE PROCEDURE before_insert_entry();


--
-- TOC entry 1946 (class 2606 OID 16401)
-- Name: entry_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entry
    ADD CONSTRAINT entry_fk FOREIGN KEY (id_project) REFERENCES project(id) ON DELETE CASCADE;


--
-- TOC entry 2064 (class 0 OID 0)
-- Dependencies: 7
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-09-05 13:19:46 ART

--
-- PostgreSQL database dump complete
--

