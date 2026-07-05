package negocio;

import modelo.Administrador;
import modelo.CalendarioTuristico;
import modelo.GuiaTuristico;
import modelo.Hotel;
import modelo.LugarTuristico;
import modelo.PaqueteTuristico;
import modelo.Recomendacion;
import modelo.Reserva;
import modelo.SalidaProgramada;
import modelo.Turista;
import modelo.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;

public class AgenciaTuristica {
    private static final String IDIOMA_PREDETERMINADO = "Español";

    public static final String TERMINAL_CARCELEN = "Terminal Terrestre de Carcelén - Quito";
    public static final String TERMINAL_QUITUMBE = "Terminal Terrestre de Quitumbe - Quito";
    public static final String AEROPUERTO_QUITO = "Aeropuerto Internacional Mariscal Sucre - Quito";

    private ArrayList<Usuario> usuarios;
    private ArrayList<LugarTuristico> lugares;
    private ArrayList<Hotel> hoteles;
    private ArrayList<PaqueteTuristico> paquetes;
    private ArrayList<SalidaProgramada> salidas;
    private ArrayList<Reserva> reservas;
    private ArrayList<Recomendacion> recomendaciones;

    private int siguienteUsuario;
    private int siguienteLugar;
    private int siguienteHotel;
    private int siguientePaquete;
    private int siguienteSalida;
    private int siguienteReserva;
    private int siguienteRecomendacion;

    public AgenciaTuristica() {
        usuarios = new ArrayList<Usuario>();
        lugares = new ArrayList<LugarTuristico>();
        hoteles = new ArrayList<Hotel>();
        paquetes = new ArrayList<PaqueteTuristico>();
        salidas = new ArrayList<SalidaProgramada>();
        reservas = new ArrayList<Reserva>();
        recomendaciones = new ArrayList<Recomendacion>();
        siguienteUsuario = 1;
        siguienteLugar = 1;
        siguienteHotel = 1;
        siguientePaquete = 1;
        siguienteSalida = 1;
        siguienteReserva = 1;
        siguienteRecomendacion = 1;

        try {
            cargarDatosIniciales();
        } catch (NegocioException e) {
            throw new IllegalStateException("No se pudieron cargar los datos iniciales: " + e.getMessage());
        }
    }

    private void cargarDatosIniciales() throws NegocioException {
        Administrador administrador = new Administrador(
                siguienteUsuario++, "Administrador General", "admin@agencia.ec",
                "Admin123!", LocalDate.now());
        administrador.setPreguntaSeguridad("¿Cuál es la ciudad sede de la agencia?");
        administrador.setRespuestaSeguridad("Quito");
        usuarios.add(administrador);

        GuiaTuristico santiago = crearGuiaInicial("Santiago Viteri", "santiago@agencia.ec",
                "Santiago1!", "Costa");
        GuiaTuristico mateo = crearGuiaInicial("Mateo Benitez", "mateo@agencia.ec",
                "Mateo123!", "Sierra");
        GuiaTuristico david = crearGuiaInicial("David Valle", "david@agencia.ec",
                "David123!", "Oriente");
        GuiaTuristico junhao = crearGuiaInicial("Junhao Zhao", "junhao@agencia.ec",
                "Junhao123!", "Galápagos");

        registrarTuristaConSeguridad("Juanito Perez", "juanito@correo.com",
                "Juanito1!", "Costa, Galápagos",
                "¿Cuál es su primer nombre?", "Juanito");

        LugarTuristico quito = crearLugar("Quito", "Sierra", "Pichincha, Ecuador");
        LugarTuristico manta = crearLugar("Manta", "Costa", "Manabí, Ecuador");
        LugarTuristico puertoLopez = crearLugar("Puerto López", "Costa", "Manabí, Ecuador");
        LugarTuristico islaPlata = crearLugar("Isla de la Plata", "Costa", "Manabí, Ecuador");
        LugarTuristico cotopaxi = crearLugar("Cotopaxi", "Sierra", "Cotopaxi, Ecuador");
        LugarTuristico quilotoa = crearLugar("Quilotoa", "Sierra", "Cotopaxi, Ecuador");
        LugarTuristico banos = crearLugar("Baños", "Sierra", "Tungurahua, Ecuador");
        LugarTuristico puyo = crearLugar("Puyo", "Oriente", "Pastaza, Ecuador");
        LugarTuristico comunidad = crearLugar("Comunidad Amazónica", "Oriente", "Pastaza, Ecuador");
        LugarTuristico santaCruz = crearLugar("Santa Cruz", "Galápagos", "Galápagos, Ecuador");
        LugarTuristico santaFe = crearLugar("Isla Santa Fe", "Galápagos", "Galápagos, Ecuador");

        Hotel hotelCosta = crearHotel("Hotel Pacífico", puertoLopez, 3);
        Hotel hotelSierra = crearHotel("Hostería Andina", banos, 3);
        Hotel hotelOriente = crearHotel("Lodge Selva Viva", puyo, 4);
        Hotel hotelGalapagos = crearHotel("Hotel Puerto Ayora", santaCruz, 4);
        crearHotel("Hilton Colón Quito", quito, 5);

        ArrayList<LugarTuristico> costa = new ArrayList<LugarTuristico>();
        costa.add(manta);
        costa.add(puertoLopez);
        costa.add(islaPlata);
        PaqueteTuristico paqueteCosta = crearPaquete("Costa 4 días",
                "Recorrido por Manta, Puerto López e Isla de la Plata.",
                4, 285.00, hotelCosta, costa, TERMINAL_QUITUMBE, TERMINAL_QUITUMBE);

        ArrayList<LugarTuristico> sierra = new ArrayList<LugarTuristico>();
        sierra.add(cotopaxi);
        sierra.add(quilotoa);
        sierra.add(banos);
        PaqueteTuristico paqueteSierra = crearPaquete("Sierra 4 días",
                "Ruta por Cotopaxi, Quilotoa y Baños.",
                4, 220.00, hotelSierra, sierra, TERMINAL_CARCELEN, TERMINAL_CARCELEN);

        ArrayList<LugarTuristico> oriente = new ArrayList<LugarTuristico>();
        oriente.add(puyo);
        oriente.add(comunidad);
        PaqueteTuristico paqueteOriente = crearPaquete("Oriente 4 días",
                "Experiencia en Puyo y comunidad amazónica.",
                4, 330.00, hotelOriente, oriente, TERMINAL_QUITUMBE, TERMINAL_QUITUMBE);

        ArrayList<LugarTuristico> galapagos = new ArrayList<LugarTuristico>();
        galapagos.add(santaCruz);
        galapagos.add(santaFe);
        PaqueteTuristico paqueteGalapagos = crearPaquete("Galápagos 5 días",
                "Visita a Santa Cruz e Isla Santa Fe.",
                5, 1250.00, hotelGalapagos, galapagos, AEROPUERTO_QUITO, AEROPUERTO_QUITO);

        crearSalidasTrimestrales(paqueteCosta, santiago, 12);
        crearSalidasTrimestrales(paqueteSierra, mateo, 15);
        crearSalidasTrimestrales(paqueteOriente, david, 10);
        crearSalidasTrimestrales(paqueteGalapagos, junhao, 12);
    }

    private GuiaTuristico crearGuiaInicial(String nombre, String correo, String clave,
                                            String region) throws NegocioException {
        String nombreValido = Validaciones.nombre(nombre);
        String correoValido = Validaciones.correo(correo);
        String claveValida = Validaciones.clave(clave);
        if (!Validaciones.esRegionValida(region)) {
            throw new NegocioException("La región del guía debe ser Costa, Sierra, Oriente o Galápagos.");
        }
        validarCorreoDisponible(correoValido, 0);
        String regionValida = Validaciones.normalizarRegion(region);
        GuiaTuristico guia = new GuiaTuristico(siguienteUsuario++, nombreValido,
                correoValido, claveValida, LocalDate.now(), IDIOMA_PREDETERMINADO,
                regionValida, LocalDate.of(2027, 1, 1));
        guia.setPreguntaSeguridad("¿Cuál es su región asignada?");
        guia.setRespuestaSeguridad(regionValida);
        usuarios.add(guia);
        return guia;
    }

    private void crearSalidasTrimestrales(PaqueteTuristico paquete, GuiaTuristico guia,
                                           int cupoMaximo) throws NegocioException {
        ArrayList<LocalDate> fechas = CalendarioTuristico.getFechasTrimestrales2027();
        for (LocalDate fechaSalida : fechas) {
            LocalDate fechaRetorno = fechaSalida.plusDays(paquete.getDuracionDias() - 1);
            crearSalida(paquete, guia, fechaSalida, fechaRetorno, cupoMaximo,
                    paquete.getPrecioBase(), CalendarioTuristico.getNombreTrimestre(fechaSalida));
        }
    }

    public Usuario autenticar(String correo, String clave) throws NegocioException {
        String correoValido = Validaciones.correo(correo);
        String claveValida = Validaciones.clave(clave);
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correoValido)
                    && usuario.getClave().equals(claveValida)) {
                if (!usuario.isActivo()) {
                    throw new NegocioException("El usuario se encuentra inhabilitado.");
                }
                return usuario;
            }
        }
        throw new NegocioException("Correo o contraseña incorrectos.");
    }

    public void restablecerContrasena(String correo, String respuesta,
                                        String nuevaClave) throws NegocioException {
        String correoValido = Validaciones.correo(correo);
        String respuestaValida = Validaciones.textoObligatorio(
                respuesta, "respuesta de seguridad");
        String claveValida = Validaciones.clave(nuevaClave);

        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correoValido)) {
                if (!usuario.isActivo()) {
                    throw new NegocioException("El usuario se encuentra inhabilitado.");
                }
                if (usuario.getPreguntaSeguridad() == null
                        || usuario.getPreguntaSeguridad().trim().isEmpty()
                        || usuario.getRespuestaSeguridad() == null
                        || usuario.getRespuestaSeguridad().trim().isEmpty()) {
                    throw new NegocioException(
                            "Este usuario no tiene configurada una pregunta de seguridad.");
                }
                if (!usuario.getRespuestaSeguridad().equalsIgnoreCase(respuestaValida)) {
                    throw new NegocioException(
                            "La respuesta a la pregunta de seguridad es incorrecta.");
                }
                if (usuario.getClave().equals(claveValida)) {
                    throw new NegocioException(
                            "La nueva contraseña debe ser diferente de la contraseña actual.");
                }
                usuario.setClave(claveValida);
                return;
            }
        }
        throw new NegocioException("Correo no encontrado en el sistema.");
    }

    public String obtenerPreguntaSeguridad(String correo) throws NegocioException {
        String correoValido = Validaciones.correo(correo);
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correoValido)) {
                if (usuario.getRespuestaSeguridad() == null || usuario.getRespuestaSeguridad().isEmpty()) {
                    throw new NegocioException("Este usuario no tiene configurada una pregunta de seguridad.");
                }
                return usuario.getPreguntaSeguridad();
            }
        }
        throw new NegocioException("Correo no encontrado en el sistema.");
    }

    public Turista registrarTurista(String nombre, String correo, String clave,
                                    String interesesTexto)
            throws NegocioException {
        return registrarTuristaConSeguridad(nombre, correo, clave,
                interesesTexto, "¿Cuál es su primer nombre?", nombre.split(" ")[0]);
    }

    public Turista registrarTuristaConSeguridad(String nombre, String correo, String clave,
                                                String interesesTexto,
                                                String preguntaSeguridad,
                                                String respuestaSeguridad)
            throws NegocioException {
        String nombreValido = Validaciones.nombre(nombre);
        String correoValido = Validaciones.correo(correo);
        String claveValida = Validaciones.clave(clave);
        ArrayList<String> interesesValidos = Validaciones.intereses(interesesTexto);
        String preguntaValida = Validaciones.textoObligatorio(
                preguntaSeguridad, "pregunta de seguridad");
        String respuestaValida = Validaciones.textoObligatorio(
                respuestaSeguridad, "respuesta de seguridad");
        validarCorreoDisponible(correoValido, 0);

        Turista turista = new Turista(siguienteUsuario++, nombreValido, correoValido,
                claveValida, LocalDate.now(), IDIOMA_PREDETERMINADO, interesesValidos);
        turista.setPreguntaSeguridad(preguntaValida);
        turista.setRespuestaSeguridad(respuestaValida);
        usuarios.add(turista);
        return turista;
    }

    public GuiaTuristico crearGuia(String nombre, String correo, String clave,
                                    String region)
            throws NegocioException {
        return crearGuiaConSeguridad(nombre, correo, clave, region,
                "¿Cuál es su región asignada?", region);
    }

    public GuiaTuristico crearGuiaConSeguridad(String nombre, String correo, String clave,
                                                String region,
                                                String preguntaSeguridad,
                                                String respuestaSeguridad)
            throws NegocioException {
        String nombreValido = Validaciones.nombre(nombre);
        String correoValido = Validaciones.correo(correo);
        String claveValida = Validaciones.clave(clave);
        String preguntaValida = Validaciones.textoObligatorio(
                preguntaSeguridad, "pregunta de seguridad");
        String respuestaValida = Validaciones.textoObligatorio(
                respuestaSeguridad, "respuesta de seguridad");
        if (!Validaciones.esRegionValida(region)) {
            throw new NegocioException(
                    "La región del guía debe ser Costa, Sierra, Oriente o Galápagos.");
        }
        validarCorreoDisponible(correoValido, 0);

        String regionValida = Validaciones.normalizarRegion(region);
        LocalDate fechaDisponible = calcularProximoTrimestreDisponible(regionValida);
        GuiaTuristico guia = new GuiaTuristico(siguienteUsuario++, nombreValido,
                correoValido, claveValida, LocalDate.now(), IDIOMA_PREDETERMINADO,
                regionValida, fechaDisponible);
        guia.setPreguntaSeguridad(preguntaValida);
        guia.setRespuestaSeguridad(respuestaValida);
        usuarios.add(guia);
        asignarGuiaAlTrimestreCorrespondiente(guia);
        return guia;
    }

    public void actualizarUsuario(Usuario usuario, String nombre, String correo,
                                  String clave, String datoRol)
            throws NegocioException {
        if (usuario == null) {
            throw new NegocioException("Debe seleccionar un usuario.");
        }

        String nombreValido = Validaciones.nombre(nombre);
        String correoValido = Validaciones.correo(correo);
        String claveValida = Validaciones.clave(clave);
        ArrayList<String> interesesValidos = null;
        String regionNueva = null;
        LocalDate nuevaFechaDisponible = null;

        validarCorreoDisponible(correoValido, usuario.getId());

        if (usuario instanceof Turista) {
            interesesValidos = Validaciones.intereses(datoRol);
        } else if (usuario instanceof GuiaTuristico) {
            if (!Validaciones.esRegionValida(datoRol)) {
                throw new NegocioException(
                        "La región del guía debe ser Costa, Sierra, Oriente o Galápagos.");
            }
            GuiaTuristico guia = (GuiaTuristico) usuario;
            regionNueva = Validaciones.normalizarRegion(datoRol);
            if (!guia.getRegionAsignada().equalsIgnoreCase(regionNueva)) {
                if (guiaTieneSalidas(guia)) {
                    throw new NegocioException(
                            "No se puede cambiar la región de un guía que ya tiene salidas asignadas.");
                }
                nuevaFechaDisponible = calcularProximoTrimestreDisponible(regionNueva);
            }
        }

        usuario.setNombre(nombreValido);
        usuario.setCorreo(correoValido);
        usuario.setClave(claveValida);

        if (usuario instanceof Turista) {
            Turista turista = (Turista) usuario;
            turista.setIntereses(interesesValidos);
        } else if (usuario instanceof GuiaTuristico) {
            GuiaTuristico guia = (GuiaTuristico) usuario;
            if (regionNueva != null
                    && !guia.getRegionAsignada().equalsIgnoreCase(regionNueva)) {
                guia.setRegionAsignada(regionNueva);
                guia.setFechaDisponibleDesde(nuevaFechaDisponible);
                asignarGuiaAlTrimestreCorrespondiente(guia);
            }
        }
    }

    private boolean guiaTieneSalidas(GuiaTuristico guia) {
        for (SalidaProgramada salida : salidas) {
            if (salida.getGuia().getId() == guia.getId()) {
                return true;
            }
        }
        return false;
    }


    private LocalDate calcularProximoTrimestreDisponible(String region) throws NegocioException {
        ArrayList<LocalDate> fechasTrimestrales = CalendarioTuristico.getFechasTrimestrales2027();
        for (LocalDate fecha : fechasTrimestrales) {
            boolean trimestreOcupado = false;
            for (GuiaTuristico guia : getGuias()) {
                if (guia.getRegionAsignada().equalsIgnoreCase(region)
                        && guia.getFechaDisponibleDesde().equals(fecha)) {
                    trimestreOcupado = true;
                    break;
                }
            }
            if (!trimestreOcupado) {
                return fecha;
            }
        }
        throw new NegocioException("La región " + region
                + " ya tiene guías asignados para los cuatro trimestres de 2027.");
    }

    private void asignarGuiaAlTrimestreCorrespondiente(GuiaTuristico guia) {
        for (SalidaProgramada salida : salidas) {
            if (salida.isActiva()
                    && salida.getPaquete().getRegion().equalsIgnoreCase(guia.getRegionAsignada())
                    && salida.getFechaSalida().equals(guia.getFechaDisponibleDesde())) {
                salida.setGuia(guia);
            }
        }
    }

    public String eliminarOInhabilitarUsuario(Usuario usuario) throws NegocioException {
        if (usuario == null) {
            throw new NegocioException("Debe seleccionar un usuario.");
        }
        if (usuario instanceof Administrador) {
            throw new NegocioException("No se puede eliminar al administrador principal.");
        }

        boolean tieneHistorial = false;
        if (usuario instanceof Turista) {
            for (Reserva reserva : reservas) {
                if (reserva.getTurista().getId() == usuario.getId()) {
                    tieneHistorial = true;
                }
            }
        }
        if (usuario instanceof GuiaTuristico) {
            for (SalidaProgramada salida : salidas) {
                if (salida.getGuia().getId() == usuario.getId()) {
                    tieneHistorial = true;
                }
            }
        }

        if (tieneHistorial) {
            usuario.setActivo(false);
            return "El usuario tiene historial y fue inhabilitado.";
        }
        usuarios.remove(usuario);
        return "Usuario eliminado correctamente.";
    }

    private void validarCorreoDisponible(String correo, int idIgnorado) throws NegocioException {
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo) && usuario.getId() != idIgnorado) {
                throw new NegocioException("El correo ya se encuentra registrado.");
            }
        }
    }

    public LugarTuristico crearLugar(String nombre, String region, String ubicacion) throws NegocioException {
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre del lugar");
        if (!Validaciones.esRegionValida(region)) {
            throw new NegocioException("La región debe ser Costa, Sierra, Oriente o Galápagos.");
        }
        String regionValida = Validaciones.normalizarRegion(region);
        String ubicacionValida = Validaciones.textoObligatorio(ubicacion, "ubicación");
        for (LugarTuristico existente : lugares) {
            if (existente.getNombre().equalsIgnoreCase(nombreValido)
                    && existente.getRegion().equalsIgnoreCase(regionValida)) {
                throw new NegocioException("Ese lugar ya existe en la región " + regionValida + ".");
            }
        }
        LugarTuristico lugar = new LugarTuristico(siguienteLugar++, nombreValido,
                regionValida, ubicacionValida);
        lugares.add(lugar);
        return lugar;
    }

    public void actualizarLugar(LugarTuristico lugar, String nombre,
                                String region, String ubicacion) throws NegocioException {
        if (lugar == null) {
            throw new NegocioException("Debe seleccionar un lugar.");
        }
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre del lugar");
        if (!Validaciones.esRegionValida(region)) {
            throw new NegocioException("La región debe ser Costa, Sierra, Oriente o Galápagos.");
        }
        String regionValida = Validaciones.normalizarRegion(region);
        for (LugarTuristico existente : lugares) {
            if (existente.getId() != lugar.getId()
                    && existente.getNombre().equalsIgnoreCase(nombreValido)
                    && existente.getRegion().equalsIgnoreCase(regionValida)) {
                throw new NegocioException("Ese lugar ya existe en la región " + regionValida + ".");
            }
        }
        if (!lugar.getRegion().equalsIgnoreCase(regionValida)) {
            for (PaqueteTuristico paquete : paquetes) {
                if (paquete.getLugares().contains(lugar)) {
                    throw new NegocioException("No se puede cambiar la región porque el lugar pertenece a un paquete.");
                }
            }
            for (Hotel hotel : hoteles) {
                if (hotel.getLugar().getId() == lugar.getId()) {
                    throw new NegocioException("No se puede cambiar la región porque existe un hotel en este lugar.");
                }
            }
        }
        lugar.setNombre(nombreValido);
        lugar.setRegion(regionValida);
        lugar.setUbicacion(Validaciones.textoObligatorio(ubicacion, "ubicación"));
    }

    public String eliminarLugar(LugarTuristico lugar) throws NegocioException {
        if (lugar == null) {
            throw new NegocioException("Debe seleccionar un lugar.");
        }
        for (PaqueteTuristico paquete : paquetes) {
            if (paquete.getLugares().contains(lugar)) {
                lugar.setActivo(false);
                return "El lugar está relacionado con un paquete y fue inhabilitado.";
            }
        }
        for (Hotel hotel : hoteles) {
            if (hotel.getLugar().getId() == lugar.getId()) {
                lugar.setActivo(false);
                return "El lugar tiene hoteles relacionados y fue inhabilitado.";
            }
        }
        lugares.remove(lugar);
        return "Lugar eliminado correctamente.";
    }

    public Hotel crearHotel(String nombre, LugarTuristico lugar, int categoria) throws NegocioException {
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre del hotel");
        if (lugar == null || !lugar.isActivo()) {
            throw new NegocioException("Debe seleccionar un lugar activo para el hotel. Si no existe, créelo primero.");
        }
        if (categoria < 1 || categoria > 5) {
            throw new NegocioException("La categoría del hotel debe estar entre 1 y 5 estrellas.");
        }
        for (Hotel existente : hoteles) {
            if (existente.getNombre().equalsIgnoreCase(nombreValido)
                    && existente.getLugar().getId() == lugar.getId()) {
                throw new NegocioException("Ya existe un hotel con ese nombre en " + lugar.getNombre() + ".");
            }
        }
        Hotel hotel = new Hotel(siguienteHotel++, nombreValido, lugar, categoria);
        hoteles.add(hotel);
        return hotel;
    }

    public void actualizarHotel(Hotel hotel, String nombre, LugarTuristico lugar,
                                int categoria) throws NegocioException {
        if (hotel == null) {
            throw new NegocioException("Debe seleccionar un hotel.");
        }
        if (lugar == null || !lugar.isActivo()) {
            throw new NegocioException("Debe seleccionar un lugar activo para el hotel. Si no existe, créelo primero.");
        }
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre del hotel");
        for (Hotel existente : hoteles) {
            if (existente.getId() != hotel.getId()
                    && existente.getNombre().equalsIgnoreCase(nombreValido)
                    && existente.getLugar().getId() == lugar.getId()) {
                throw new NegocioException("Ya existe un hotel con ese nombre en " + lugar.getNombre() + ".");
            }
        }
        if (categoria < 1 || categoria > 5) {
            throw new NegocioException("La categoría del hotel debe estar entre 1 y 5 estrellas.");
        }
        for (PaqueteTuristico paquete : paquetes) {
            if (paquete.getHotel().getId() == hotel.getId()
                    && !paquete.getLugares().contains(lugar)) {
                throw new NegocioException("No se puede mover el hotel a " + lugar.getNombre()
                        + " porque ese lugar no forma parte del paquete " + paquete.getNombre() + ".");
            }
        }
        hotel.setNombre(nombreValido);
        hotel.setLugar(lugar);
        hotel.setCategoria(categoria);
    }

    public String eliminarHotel(Hotel hotel) throws NegocioException {
        if (hotel == null) {
            throw new NegocioException("Debe seleccionar un hotel.");
        }
        for (PaqueteTuristico paquete : paquetes) {
            if (paquete.getHotel().getId() == hotel.getId()) {
                hotel.setActivo(false);
                return "El hotel está relacionado con un paquete y fue inhabilitado.";
            }
        }
        hoteles.remove(hotel);
        return "Hotel eliminado correctamente.";
    }

    public PaqueteTuristico crearPaquete(String nombre, String descripcion,
                                         int duracionDias, double precioBase,
                                         Hotel hotel, ArrayList<LugarTuristico> lugaresSeleccionados,
                                         String lugarSalida, String lugarRegreso)
            throws NegocioException {
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre del paquete");
        String descripcionValida = Validaciones.textoObligatorio(descripcion, "descripción");
        Validaciones.enteroPositivo(duracionDias, "duración");
        Validaciones.decimalPositivo(precioBase, "precio base");
        validarNombrePaqueteDisponible(nombreValido, 0);
        validarDatosPaquete(hotel, lugaresSeleccionados, lugarSalida, lugarRegreso);
        PaqueteTuristico paquete = new PaqueteTuristico(siguientePaquete++, nombreValido,
                descripcionValida, duracionDias, precioBase, hotel,
                new ArrayList<LugarTuristico>(lugaresSeleccionados),
                lugarSalida, lugarRegreso);
        paquetes.add(paquete);
        return paquete;
    }

    public void actualizarPaquete(PaqueteTuristico paquete, String nombre,
                                  String descripcion, int duracionDias, double precioBase,
                                  Hotel hotel, ArrayList<LugarTuristico> lugaresSeleccionados,
                                  String lugarSalida, String lugarRegreso)
            throws NegocioException {
        if (paquete == null) {
            throw new NegocioException("Debe seleccionar un paquete.");
        }
        String nombreValido = Validaciones.textoObligatorio(nombre, "nombre del paquete");
        String descripcionValida = Validaciones.textoObligatorio(descripcion, "descripción");
        int duracionValida = Validaciones.enteroPositivo(duracionDias, "duración");
        double precioValido = Validaciones.decimalPositivo(precioBase, "precio base");
        validarNombrePaqueteDisponible(nombreValido, paquete.getId());
        validarDatosPaquete(hotel, lugaresSeleccionados, lugarSalida, lugarRegreso);

        paquete.setNombre(nombreValido);
        paquete.setDescripcion(descripcionValida);
        paquete.setDuracionDias(duracionValida);
        paquete.setPrecioBase(precioValido);
        paquete.setHotel(hotel);
        paquete.setLugares(new ArrayList<LugarTuristico>(lugaresSeleccionados));
        paquete.setLugarSalida(lugarSalida);
        paquete.setLugarRegreso(lugarRegreso);
    }

    private void validarNombrePaqueteDisponible(String nombre, int idIgnorado)
            throws NegocioException {
        for (PaqueteTuristico existente : paquetes) {
            if (existente.getId() != idIgnorado
                    && existente.getNombre().equalsIgnoreCase(nombre)) {
                throw new NegocioException(
                        "Ya existe un paquete turístico con ese nombre.");
            }
        }
    }

    private void validarDatosPaquete(Hotel hotel, ArrayList<LugarTuristico> lugaresSeleccionados,
                                     String lugarSalida, String lugarRegreso) throws NegocioException {
        if (hotel == null || !hotel.isActivo()) {
            throw new NegocioException("Debe seleccionar un hotel activo.");
        }
        if (lugaresSeleccionados == null || lugaresSeleccionados.isEmpty()) {
            throw new NegocioException("El paquete debe tener al menos un lugar turístico.");
        }
        validarLugaresMismaRegion(lugaresSeleccionados);
        boolean contieneLugarHotel = false;
        for (LugarTuristico lugar : lugaresSeleccionados) {
            if (lugar.getId() == hotel.getLugar().getId()) {
                contieneLugarHotel = true;
            }
        }
        if (!contieneLugarHotel) {
            throw new NegocioException("El lugar del hotel (" + hotel.getLugar().getNombre()
                    + ") debe estar incluido entre los lugares del paquete.");
        }
        String salidaValida = validarPuntoQuito(lugarSalida, "lugar de salida");
        String regresoValido = validarPuntoQuito(lugarRegreso, "lugar de regreso");
        String region = lugaresSeleccionados.get(0).getRegion();
        if (region.equalsIgnoreCase("Galápagos")
                && (!salidaValida.equals(AEROPUERTO_QUITO) || !regresoValido.equals(AEROPUERTO_QUITO))) {
            throw new NegocioException("Los paquetes de Galápagos deben salir y regresar por el Aeropuerto de Quito.");
        }
        if (!region.equalsIgnoreCase("Galápagos")
                && (salidaValida.equals(AEROPUERTO_QUITO) || regresoValido.equals(AEROPUERTO_QUITO))) {
            throw new NegocioException("Los paquetes de Costa, Sierra y Oriente deben usar Carcelén o Quitumbe.");
        }
    }

    private String validarPuntoQuito(String punto, String campo) throws NegocioException {
        String valor = Validaciones.textoObligatorio(punto, campo);
        if (!valor.equals(TERMINAL_CARCELEN) && !valor.equals(TERMINAL_QUITUMBE)
                && !valor.equals(AEROPUERTO_QUITO)) {
            throw new NegocioException("El " + campo + " debe ser Carcelén, Quitumbe o el Aeropuerto de Quito.");
        }
        return valor;
    }

    private void validarLugaresMismaRegion(ArrayList<LugarTuristico> lugaresSeleccionados)
            throws NegocioException {
        String region = lugaresSeleccionados.get(0).getRegion();
        for (LugarTuristico lugar : lugaresSeleccionados) {
            if (!lugar.isActivo()) {
                throw new NegocioException("Todos los lugares seleccionados deben estar activos.");
            }
            if (!lugar.getRegion().equalsIgnoreCase(region)) {
                throw new NegocioException("Todos los lugares de un paquete deben pertenecer a la misma región.");
            }
        }
    }

    public String eliminarPaquete(PaqueteTuristico paquete) throws NegocioException {
        if (paquete == null) {
            throw new NegocioException("Debe seleccionar un paquete.");
        }
        for (SalidaProgramada salida : salidas) {
            if (salida.getPaquete().getId() == paquete.getId()) {
                paquete.setActivo(false);
                return "El paquete tiene salidas relacionadas y fue inhabilitado.";
            }
        }
        paquetes.remove(paquete);
        return "Paquete eliminado correctamente.";
    }

    public SalidaProgramada crearSalida(PaqueteTuristico paquete, GuiaTuristico guia,
                                        LocalDate fechaSalida, LocalDate fechaRetorno,
                                        int cupoMaximo, double precioFinal, String temporada)
            throws NegocioException {
        if (paquete == null || !paquete.isActivo()) {
            throw new NegocioException("Debe seleccionar un paquete activo.");
        }
        if (guia == null || !guia.isActivo()) {
            throw new NegocioException("Debe seleccionar un guía activo.");
        }
        validarReglasSalida(paquete, guia, fechaSalida, fechaRetorno, 0);
        Validaciones.enteroPositivo(cupoMaximo, "cupo máximo");
        Validaciones.decimalPositivo(precioFinal, "precio final");
        String temporadaValida = Validaciones.textoObligatorio(temporada, "temporada");
        validarDisponibilidadGuia(guia, fechaSalida, fechaRetorno, 0);

        SalidaProgramada salida = new SalidaProgramada(siguienteSalida++, paquete, guia,
                fechaSalida, fechaRetorno, cupoMaximo, precioFinal, temporadaValida);
        salidas.add(salida);
        return salida;
    }

    public void actualizarSalida(SalidaProgramada salida, PaqueteTuristico paquete,
                                 GuiaTuristico guia, LocalDate fechaSalida,
                                 LocalDate fechaRetorno, int cupoMaximo,
                                 double precioFinal, String temporada) throws NegocioException {
        if (salida == null) {
            throw new NegocioException("Debe seleccionar una salida.");
        }
        if (paquete == null || guia == null) {
            throw new NegocioException("Debe seleccionar paquete y guía.");
        }
        validarReglasSalida(paquete, guia, fechaSalida, fechaRetorno, salida.getId());
        Validaciones.enteroPositivo(cupoMaximo, "cupo máximo");
        int reservasActivas = 0;
        for (Reserva reserva : salida.getReservas()) {
            if (!reserva.getEstado().equals("CANCELADA")) {
                reservasActivas++;
            }
        }
        if (cupoMaximo < reservasActivas) {
            throw new NegocioException(
                    "El cupo máximo no puede ser menor a las reservas activas existentes.");
        }
        Validaciones.decimalPositivo(precioFinal, "precio final");
        String temporadaValida = Validaciones.textoObligatorio(temporada, "temporada");
        validarDisponibilidadGuia(guia, fechaSalida, fechaRetorno, salida.getId());

        salida.setPaquete(paquete);
        salida.setGuia(guia);
        salida.setFechaSalida(fechaSalida);
        salida.setFechaRetorno(fechaRetorno);
        salida.setCupoMaximo(cupoMaximo);
        salida.setPrecioFinal(precioFinal);
        salida.setTemporada(temporadaValida);
    }

    private void validarReglasSalida(PaqueteTuristico paquete, GuiaTuristico guia,
                                      LocalDate fechaSalida, LocalDate fechaRetorno,
                                      int idIgnorado) throws NegocioException {
        Validaciones.rangoFechas(fechaSalida, fechaRetorno);
        if (!CalendarioTuristico.esFechaTrimestral2027(fechaSalida)) {
            throw new NegocioException("Las salidas solo pueden iniciar el 1 de enero, abril, julio u octubre de 2027.");
        }
        LocalDate retornoEsperado = fechaSalida.plusDays(paquete.getDuracionDias() - 1);
        if (!fechaRetorno.equals(retornoEsperado)) {
            throw new NegocioException("La fecha de retorno debe corresponder a la duración del paquete.");
        }
        if (!guia.puedeCubrir(paquete.getRegion(), fechaSalida)) {
            throw new NegocioException("El guía debe cubrir la región " + paquete.getRegion()
                    + " y estar disponible para ese trimestre.");
        }
        for (SalidaProgramada salida : salidas) {
            if (salida.getId() != idIgnorado
                    && salida.getPaquete().getId() == paquete.getId()
                    && salida.getFechaSalida().equals(fechaSalida)) {
                throw new NegocioException("Este paquete ya tiene una salida registrada para ese trimestre.");
            }
        }
    }

    private void validarDisponibilidadGuia(GuiaTuristico guia, LocalDate inicio,
                                           LocalDate fin, int idIgnorado) throws NegocioException {
        for (SalidaProgramada salida : salidas) {
            if (salida.getId() != idIgnorado && salida.isActiva()
                    && salida.getGuia().getId() == guia.getId()) {
                boolean seCruzan = !fin.isBefore(salida.getFechaSalida())
                        && !inicio.isAfter(salida.getFechaRetorno());
                if (seCruzan) {
                    throw new NegocioException("El guía ya tiene una salida programada en esas fechas.");
                }
            }
        }
    }

    public String eliminarSalida(SalidaProgramada salida) throws NegocioException {
        if (salida == null) {
            throw new NegocioException("Debe seleccionar una salida.");
        }
        if (!salida.getReservas().isEmpty()) {
            salida.setActiva(false);
            return "La salida tiene reservas y fue inhabilitada.";
        }
        salidas.remove(salida);
        return "Salida eliminada correctamente.";
    }

    public Reserva reservar(Turista turista, SalidaProgramada salida) throws NegocioException {
        if (turista == null || !turista.isActivo()) {
            throw new NegocioException("El turista no está disponible.");
        }
        if (salida == null || !salida.isActiva()) {
            throw new NegocioException("La salida no está disponible.");
        }
        if (!salida.getFechaSalida().isAfter(LocalDate.now())) {
            throw new NegocioException("No se puede reservar una salida que ya inició o finalizó.");
        }
        if (salida.getCuposDisponibles() <= 0) {
            throw new NegocioException("La salida ya no tiene cupos disponibles.");
        }
        for (Reserva reserva : reservas) {
            if (reserva.getTurista().getId() == turista.getId()
                    && reserva.getSalida().getId() == salida.getId()
                    && !reserva.getEstado().equals("CANCELADA")) {
                throw new NegocioException("El turista ya reservó esta salida trimestral.");
            }
        }
        Reserva reserva = new Reserva(siguienteReserva++, turista, salida,
                LocalDate.now(), "RESERVADA");
        reservas.add(reserva);
        salida.agregarReserva(reserva);
        return reserva;
    }

    private Reserva crearReservaInicial(Turista turista, SalidaProgramada salida,
                                        LocalDate fecha, String estado) {
        Reserva reserva = new Reserva(siguienteReserva++, turista, salida, fecha, estado);
        reservas.add(reserva);
        salida.agregarReserva(reserva);
        return reserva;
    }

    public void cancelarReserva(Reserva reserva, Turista turista) throws NegocioException {
        validarReservaCancelable(reserva, turista);
        reserva.setEstado("CANCELADA");
    }

    public Reserva reagendarReserva(Reserva reserva, Turista turista) throws NegocioException {
        validarReservaCancelable(reserva, turista);

        SalidaProgramada siguienteSalida = buscarSiguienteSalidaDisponible(reserva, turista);
        if (siguienteSalida == null) {
            throw new NegocioException(
                    "No existe una siguiente fecha disponible para reagendar este paquete.");
        }

        reserva.setEstado("CANCELADA");
        Reserva nuevaReserva = new Reserva(siguienteReserva++, turista, siguienteSalida,
                LocalDate.now(), "RESERVADA");
        reservas.add(nuevaReserva);
        siguienteSalida.agregarReserva(nuevaReserva);
        return nuevaReserva;
    }

    private void validarReservaCancelable(Reserva reserva, Turista turista) throws NegocioException {
        if (reserva == null) {
            throw new NegocioException("Debe seleccionar una reserva.");
        }
        if (reserva.getTurista().getId() != turista.getId()) {
            throw new NegocioException("La reserva no pertenece al turista.");
        }
        if (!reserva.getEstado().equals("RESERVADA")) {
            throw new NegocioException("Solo se pueden cancelar reservas activas.");
        }
        if (!reserva.getSalida().getFechaSalida().isAfter(LocalDate.now())) {
            throw new NegocioException("No se puede cancelar una salida que ya inició.");
        }
    }

    private SalidaProgramada buscarSiguienteSalidaDisponible(Reserva reserva, Turista turista) {
        SalidaProgramada salidaActual = reserva.getSalida();
        SalidaProgramada siguienteSalida = null;

        for (SalidaProgramada salida : salidas) {
            boolean mismoPaquete = salida.getPaquete().getId() == salidaActual.getPaquete().getId();
            boolean fechaPosterior = salida.getFechaSalida().isAfter(salidaActual.getFechaSalida());
            boolean disponible = salida.isActiva()
                    && salida.getPaquete().isActivo()
                    && salida.getGuia() != null
                    && salida.getGuia().isActivo()
                    && salida.getFechaSalida().isAfter(LocalDate.now())
                    && salida.getCuposDisponibles() > 0
                    && !tieneReservaEnSalida(turista, salida);

            if (mismoPaquete && fechaPosterior && disponible) {
                if (siguienteSalida == null
                        || salida.getFechaSalida().isBefore(siguienteSalida.getFechaSalida())) {
                    siguienteSalida = salida;
                }
            }
        }
        return siguienteSalida;
    }

    public void cambiarEstadoReserva(Reserva reserva, String estado)
            throws NegocioException {
        if (reserva == null) {
            throw new NegocioException("Debe seleccionar una reserva.");
        }
        if (!estado.equals("RESERVADA") && !estado.equals("REALIZADA")
                && !estado.equals("CANCELADA")) {
            throw new NegocioException("Estado de reserva no válido.");
        }

        // El administrador tiene control total sobre el estado de las reservas.
        // Puede corregirlo manualmente sin restricciones por las fechas del viaje.
        reserva.setEstado(estado);
    }

    public Recomendacion registrarRecomendacion(Turista turista, Reserva reserva,
                                                 int calificacionGuia,
                                                 int calificacionExperiencia,
                                                 String comentario) throws NegocioException {
        if (turista == null || reserva == null) {
            throw new NegocioException("Debe seleccionar una reserva realizada.");
        }
        if (reserva.getTurista().getId() != turista.getId()) {
            throw new NegocioException("La reserva no pertenece al turista.");
        }
        if (!reserva.getEstado().equals("REALIZADA")) {
            throw new NegocioException("Solo puede recomendar viajes que ya fueron realizados.");
        }
        if (tieneRecomendacion(reserva)) {
            throw new NegocioException("Esta reserva ya tiene una recomendación registrada.");
        }
        Validaciones.calificacion(calificacionGuia, "calificación del guía");
        Validaciones.calificacion(calificacionExperiencia, "calificación de la experiencia");
        String comentarioValido = Validaciones.textoObligatorio(comentario, "comentario");

        Recomendacion recomendacion = new Recomendacion(siguienteRecomendacion++, turista,
                reserva, calificacionGuia, calificacionExperiencia,
                comentarioValido, LocalDate.now());
        recomendaciones.add(recomendacion);
        return recomendacion;
    }

    public boolean tieneRecomendacion(Reserva reserva) {
        for (Recomendacion recomendacion : recomendaciones) {
            if (recomendacion.getReserva().getId() == reserva.getId()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<LugarTuristico> getLugaresActivosPorRegion(String region) {
        ArrayList<LugarTuristico> resultado = new ArrayList<LugarTuristico>();
        for (LugarTuristico lugar : lugares) {
            if (lugar.isActivo() && lugar.getRegion().equalsIgnoreCase(region)) {
                resultado.add(lugar);
            }
        }
        return resultado;
    }

    public ArrayList<Hotel> getHotelesActivosPorRegion(String region) {
        ArrayList<Hotel> resultado = new ArrayList<Hotel>();
        for (Hotel hotel : hoteles) {
            if (hotel.isActivo() && hotel.getRegion().equalsIgnoreCase(region)) {
                resultado.add(hotel);
            }
        }
        return resultado;
    }

    public String[] getPuntosSalidaQuito() {
        return new String[]{TERMINAL_CARCELEN, TERMINAL_QUITUMBE, AEROPUERTO_QUITO};
    }

    public void actualizarEstadosPorFecha() {
        LocalDate hoy = LocalDate.now();
        for (Reserva reserva : reservas) {
            if (reserva.getEstado().equals("RESERVADA")
                    && reserva.getSalida().getFechaRetorno().isBefore(hoy)) {
                reserva.setEstado("REALIZADA");
            }
        }
    }

    public ArrayList<Reserva> getReservasParaUsuario(Usuario usuario) {
        actualizarEstadosPorFecha();
        ArrayList<Reserva> resultado = new ArrayList<Reserva>();
        if (usuario instanceof Administrador) {
            resultado.addAll(reservas);
        } else if (usuario instanceof Turista) {
            resultado.addAll(getReservasTurista((Turista) usuario));
        } else if (usuario instanceof GuiaTuristico) {
            GuiaTuristico guia = (GuiaTuristico) usuario;
            for (Reserva reserva : reservas) {
                if (reserva.getSalida().getGuia().getId() == guia.getId()) {
                    resultado.add(reserva);
                }
            }
        }
        return resultado;
    }

    public ArrayList<Usuario> getUsuarios() {
        return new ArrayList<Usuario>(usuarios);
    }

    public ArrayList<Turista> getTuristas() {
        ArrayList<Turista> resultado = new ArrayList<Turista>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Turista) {
                resultado.add((Turista) usuario);
            }
        }
        return resultado;
    }

    public ArrayList<GuiaTuristico> getGuias() {
        ArrayList<GuiaTuristico> resultado = new ArrayList<GuiaTuristico>();
        for (Usuario usuario : usuarios) {
            if (usuario instanceof GuiaTuristico) {
                resultado.add((GuiaTuristico) usuario);
            }
        }
        return resultado;
    }

    public ArrayList<LugarTuristico> getLugares() {
        return new ArrayList<LugarTuristico>(lugares);
    }

    public ArrayList<Hotel> getHoteles() {
        return new ArrayList<Hotel>(hoteles);
    }

    public ArrayList<PaqueteTuristico> getPaquetes() {
        return new ArrayList<PaqueteTuristico>(paquetes);
    }

    public ArrayList<SalidaProgramada> getSalidas() {
        return new ArrayList<SalidaProgramada>(salidas);
    }

    public ArrayList<Reserva> getReservas() {
        return new ArrayList<Reserva>(reservas);
    }

    public ArrayList<Recomendacion> getRecomendaciones() {
        return new ArrayList<Recomendacion>(recomendaciones);
    }

    public ArrayList<SalidaProgramada> getSalidasDisponibles() {
        ArrayList<SalidaProgramada> resultado = new ArrayList<SalidaProgramada>();
        for (SalidaProgramada salida : salidas) {
            if (salida.isActiva() && salida.getPaquete().isActivo()
                    && salida.getGuia() != null && salida.getGuia().isActivo()
                    && salida.getFechaSalida().isAfter(LocalDate.now())
                    && salida.getCuposDisponibles() > 0) {
                resultado.add(salida);
            }
        }
        return resultado;
    }

    public ArrayList<SalidaProgramada> getSalidasDisponibles(Turista turista) {
        ArrayList<SalidaProgramada> resultado = new ArrayList<SalidaProgramada>();
        for (SalidaProgramada salida : getSalidasDisponibles()) {
            if (!tieneReservaEnSalida(turista, salida)) {
                resultado.add(salida);
            }
        }
        return resultado;
    }

    public boolean tieneReservaEnSalida(Turista turista, SalidaProgramada salida) {
        for (Reserva reserva : reservas) {
            if (reserva.getTurista().getId() == turista.getId()
                    && reserva.getSalida().getId() == salida.getId()
                    && !reserva.getEstado().equals("CANCELADA")) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<GuiaTuristico> getGuiasDisponibles(PaqueteTuristico paquete,
                                                         LocalDate fechaSalida) {
        ArrayList<GuiaTuristico> resultado = new ArrayList<GuiaTuristico>();
        if (paquete == null || fechaSalida == null) {
            return resultado;
        }
        for (GuiaTuristico guia : getGuias()) {
            if (guia.puedeCubrir(paquete.getRegion(), fechaSalida)) {
                boolean ocupado = false;
                LocalDate fechaRetorno = fechaSalida.plusDays(paquete.getDuracionDias() - 1);
                for (SalidaProgramada salida : salidas) {
                    if (salida.isActiva() && salida.getGuia().getId() == guia.getId()) {
                        boolean seCruzan = !fechaRetorno.isBefore(salida.getFechaSalida())
                                && !fechaSalida.isAfter(salida.getFechaRetorno());
                        if (seCruzan) {
                            ocupado = true;
                        }
                    }
                }
                if (!ocupado) {
                    resultado.add(guia);
                }
            }
        }
        return resultado;
    }

    public ArrayList<Reserva> getReservasTurista(Turista turista) {
        actualizarEstadosPorFecha();
        ArrayList<Reserva> resultado = new ArrayList<Reserva>();
        for (Reserva reserva : reservas) {
            if (reserva.getTurista().getId() == turista.getId()) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    public ArrayList<Reserva> getReservasRealizadasSinRecomendacion(Turista turista) {
        actualizarEstadosPorFecha();
        ArrayList<Reserva> resultado = new ArrayList<Reserva>();
        for (Reserva reserva : reservas) {
            if (reserva.getTurista().getId() == turista.getId()
                    && reserva.getEstado().equals("REALIZADA")
                    && !tieneRecomendacion(reserva)) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }

    public ArrayList<SalidaProgramada> getSalidasGuia(GuiaTuristico guia) {
        ArrayList<SalidaProgramada> resultado = new ArrayList<SalidaProgramada>();
        for (SalidaProgramada salida : salidas) {
            if (salida.getGuia().getId() == guia.getId()) {
                resultado.add(salida);
            }
        }
        return resultado;
    }

    public String reporteTurista(Turista turista) {
        ArrayList<Reserva> propias = getReservasTurista(turista);
        int realizadas = 0;
        int futuras = 0;
        double totalGastado = 0;
        String paqueteMasCostoso = "Sin viajes realizados";
        double costoMayor = 0;
        String paqueteMasVisitado = "Sin viajes realizados";
        int visitasMayor = 0;

        for (Reserva reserva : propias) {
            if (reserva.getEstado().equals("REALIZADA")) {
                realizadas++;
                totalGastado += reserva.getSalida().getPrecioFinal();
                if (reserva.getSalida().getPrecioFinal() > costoMayor) {
                    costoMayor = reserva.getSalida().getPrecioFinal();
                    paqueteMasCostoso = reserva.getSalida().getPaquete().getNombre();
                }
            }
            if (reserva.getEstado().equals("RESERVADA")
                    && reserva.getSalida().getFechaSalida().isAfter(LocalDate.now())) {
                futuras++;
            }
        }

        for (PaqueteTuristico paquete : paquetes) {
            int contador = 0;
            for (Reserva reserva : propias) {
                if (reserva.getEstado().equals("REALIZADA")
                        && reserva.getSalida().getPaquete().getId() == paquete.getId()) {
                    contador++;
                }
            }
            if (contador > visitasMayor) {
                visitasMayor = contador;
                paqueteMasVisitado = paquete.getNombre();
            }
        }

        return "REPORTE PERSONAL DE " + turista.getNombre().toUpperCase() + "\n\n"
                + "Reservas registradas: " + propias.size() + "\n"
                + "Viajes realizados: " + realizadas + "\n"
                + "Próximos viajes: " + futuras + "\n"
                + "Total gastado: $" + String.format("%.2f", totalGastado) + "\n"
                + "Paquete más visitado: " + paqueteMasVisitado + "\n"
                + "Viaje más costoso: " + paqueteMasCostoso + "\n";
    }

    public String reporteGuia(GuiaTuristico guia) {
        ArrayList<SalidaProgramada> propias = getSalidasGuia(guia);
        int realizadas = 0;
        int turistasAtendidos = 0;
        int sumaCalificaciones = 0;
        int cantidadCalificaciones = 0;

        for (SalidaProgramada salida : propias) {
            if (salida.getFechaRetorno().isBefore(LocalDate.now())) {
                realizadas++;
            }
            for (Reserva reserva : salida.getReservas()) {
                if (reserva.getEstado().equals("REALIZADA")) {
                    turistasAtendidos++;
                }
            }
        }

        for (Recomendacion recomendacion : recomendaciones) {
            if (recomendacion.getReserva().getSalida().getGuia().getId() == guia.getId()) {
                sumaCalificaciones += recomendacion.getCalificacionGuia();
                cantidadCalificaciones++;
            }
        }

        double promedio = 0;
        if (cantidadCalificaciones > 0) {
            promedio = (double) sumaCalificaciones / cantidadCalificaciones;
        }

        return "REPORTE DEL GUÍA " + guia.getNombre().toUpperCase() + "\n\n"
                + "Salidas asignadas: " + propias.size() + "\n"
                + "Salidas realizadas: " + realizadas + "\n"
                + "Turistas atendidos: " + turistasAtendidos + "\n"
                + "Calificaciones recibidas: " + cantidadCalificaciones + "\n"
                + "Promedio como guía: " + String.format("%.2f", promedio) + " / 5\n";
    }

    public String reporteAdministrador() {
        int turistasActivos = 0;
        int guiasActivos = 0;
        int realizadas = 0;
        double ingresos = 0;

        for (Usuario usuario : usuarios) {
            if (usuario instanceof Turista && usuario.isActivo()) {
                turistasActivos++;
            }
            if (usuario instanceof GuiaTuristico && usuario.isActivo()) {
                guiasActivos++;
            }
        }

        for (Reserva reserva : reservas) {
            if (reserva.getEstado().equals("REALIZADA")) {
                realizadas++;
                ingresos += reserva.getSalida().getPrecioFinal();
            }
        }

        Turista turistaMasViajes = null;
        int mayorViajes = 0;
        for (Turista turista : getTuristas()) {
            int contador = 0;
            for (Reserva reserva : reservas) {
                if (reserva.getTurista().getId() == turista.getId()
                        && reserva.getEstado().equals("REALIZADA")) {
                    contador++;
                }
            }
            if (contador > mayorViajes) {
                mayorViajes = contador;
                turistaMasViajes = turista;
            }
        }

        PaqueteTuristico paquetePopular = null;
        int mayorReservas = 0;
        for (PaqueteTuristico paquete : paquetes) {
            int contador = 0;
            for (Reserva reserva : reservas) {
                if (!reserva.getEstado().equals("CANCELADA")
                        && reserva.getSalida().getPaquete().getId() == paquete.getId()) {
                    contador++;
                }
            }
            if (contador > mayorReservas) {
                mayorReservas = contador;
                paquetePopular = paquete;
            }
        }

        GuiaTuristico mejorGuia = null;
        double mejorPromedio = 0;
        for (GuiaTuristico guia : getGuias()) {
            int suma = 0;
            int cantidad = 0;
            for (Recomendacion recomendacion : recomendaciones) {
                if (recomendacion.getReserva().getSalida().getGuia().getId() == guia.getId()) {
                    suma += recomendacion.getCalificacionGuia();
                    cantidad++;
                }
            }
            if (cantidad > 0) {
                double promedio = (double) suma / cantidad;
                if (promedio > mejorPromedio) {
                    mejorPromedio = promedio;
                    mejorGuia = guia;
                }
            }
        }

        String nombreTurista = "Sin datos";
        if (turistaMasViajes != null) {
            nombreTurista = turistaMasViajes.getNombre() + " (" + mayorViajes + ")";
        }
        String nombrePaquete = "Sin datos";
        if (paquetePopular != null) {
            nombrePaquete = paquetePopular.getNombre() + " (" + mayorReservas + ")";
        }
        String nombreGuia = "Sin datos";
        if (mejorGuia != null) {
            nombreGuia = mejorGuia.getNombre() + " (" + String.format("%.2f", mejorPromedio) + ")";
        }

        return "REPORTE GENERAL DE LA AGENCIA\n\n"
                + "Turistas activos: " + turistasActivos + "\n"
                + "Guías activos: " + guiasActivos + "\n"
                + "Paquetes registrados: " + paquetes.size() + "\n"
                + "Salidas programadas: " + salidas.size() + "\n"
                + "Reservas totales: " + reservas.size() + "\n"
                + "Viajes realizados: " + realizadas + "\n"
                + "Ingresos por viajes realizados: $" + String.format("%.2f", ingresos) + "\n"
                + "Turista con más viajes: " + nombreTurista + "\n"
                + "Paquete más contratado: " + nombrePaquete + "\n"
                + "Guía mejor calificado: " + nombreGuia + "\n";
    }
}
