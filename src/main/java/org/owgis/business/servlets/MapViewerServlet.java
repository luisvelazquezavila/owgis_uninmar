/*
* Copyright (c) 2013 Olmo Zavala
* Permission is hereby granted, free of charge, to any person obtaining a copy of 
* this software and associated documentation files (the "Software"), to deal in the 
* Software without restriction, including without limitation the rights to use, copy, 
* modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and 
* to permit persons to whom the Software is furnished to do so, subject to the following conditions: 
* The above copyright notice and this permission notice shall be included in all copies or substantial 
* portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
* PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
* FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
* ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
*/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owgis.business.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owgis.business.LayerMenuManagerSingleton;
import org.owgis.business.NetCDFRequestManager;
import org.owgis.business.OpenLayersManager;
import org.owgis.business.UserRequestManager;
import org.owgis.conf.OpenLayerMapConfig;
import org.owgis.exceptions.XMLFilesException;
import org.owgis.exceptions.XMLLayerException;
import org.owgis.model.Layer;
import org.owgis.model.PagesNames;
import org.owgis.model.menu.MenuEntry;
import org.owgis.model.menu.TreeMenuUtils;
import org.owgis.model.menu.TreeNode;
import org.owgis.tools.HtmlMenuBuilder;
import java.io.FileNotFoundException;

/**
 * Servelet to process all the request of the user and incharge of presenting the client
 * with the OpenLayers configuration
 *
 *
 * @author Olmo Zavala Romero
 */
@WebServlet(name = "org.owgis.business.servlets.MapViewerServlet")
public class MapViewerServlet extends HttpServlet {

	OpenLayersManager opManager;//OpenLayers Code
	OpenLayerMapConfig mapConfig;
	NetCDFRequestManager ncManager;// This object is used to manage the netcdf curr_main_layers
	String[] linksVectorialesKmz;
	String configFilePath;

	Boolean exceptionInitializingVariables = false;

	/**
	 * Initiliazes the varibles of the Servlet 
	 * @throws FileNotFoundException
	 * @throws XMLFilesException
	 * @throws XMLLayerException 
	 */
	private void initializeVariables() throws FileNotFoundException, XMLFilesException, XMLLayerException{

            System.out.println();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("### ENTRE A initializeVariables()");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            mapConfig = OpenLayerMapConfig.getInstance();

            System.out.println("### PASO 1: OpenLayerMapConfig obtenido");

            configFilePath = getServletContext().getRealPath("/WEB-INF/conf/MapViewConfig.properties");

            System.out.println("### PASO 2: configFilePath = " + configFilePath);

            mapConfig.updateProperties(configFilePath);

            System.out.println("### PASO 3: updateProperties OK");

            String layersFolder = getServletContext().getRealPath("/layers/");

            System.out.println("### PASO 4: layersFolder = " + layersFolder);

            String baseLayerMenuOrientation = mapConfig.getProperty("baseLayerMenuOrientation");

            System.out.println("### PASO 5: baseLayerMenuOrientation = " + baseLayerMenuOrientation);

            HtmlMenuBuilder.baseLayerMenuOrientation = baseLayerMenuOrientation;

            LayerMenuManagerSingleton.setLayersFolder(layersFolder);

            System.out.println("### PASO 6: setLayersFolder OK");

            opManager = new OpenLayersManager();

            System.out.println("### PASO 7: OpenLayersManager OK");

            ncManager = new NetCDFRequestManager();

            System.out.println("### PASO 8: NetCDFRequestManager OK");

            System.out.println();
            System.out.println("########################################################");
            System.out.println("### MAPVIEWER: INICIANDO CARGA INICIAL DE LAYERS");
            System.out.println("### Voy a llamar getInstance().refreshTree(true)");
            System.out.println("########################################################");

            new Exception(
                "STACK TRACE: MAPVIEWER refreshTree(true)"
            ).printStackTrace();

            LayerMenuManagerSingleton.getInstance().refreshTree(true);

            System.out.println();
            System.out.println("########################################################");
            System.out.println("### MAPVIEWER: REGRESO DE refreshTree(true)");
            System.out.println("########################################################");
        }

	/**
	 * Initializes the object that controls the access to the server
	 *
	 */
	@Override
	public void init() throws ServletException {
            
            System.out.println("################################################");
            System.out.println("### MAPVIEWERSERVLET INIT - PRUEBA 31-08-2026 ###");
            System.out.println("### CODE SOURCE = " +
                    MapViewerServlet.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation());
            System.out.println("################################################");
            
            try {
                java.io.FileWriter fw = new java.io.FileWriter(
                    "C:\\apache-tomcat-9\\mapviewer_init_test.txt",
                    true
                );
                fw.write("MAPVIEWER INIT EJECUTADO - " + new java.util.Date() + "\r\n");
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            System.err.println("########### PRUEBA MAPVIEWER INIT ###########");
            System.err.flush();

            System.out.println("########### PRUEBA MAPVIEWER INIT ###########");
            System.out.flush();
            
            System.err.println("###### MAPVIEWER INIT SYSTEM ERR ########");

            System.out.println();
            System.out.println("################################################");
            System.out.println("### MAPVIEWER INIT() EJECUTADO");
            System.out.println("### SERVLET = MapViewerServlet");
            System.out.println("### CONTEXT = " + getServletContext().getContextPath());
            System.out.println("################################################");

            try {
                initializeVariables();

                System.out.println("### MAPVIEWER initializeVariables() TERMINO");

            } catch (XMLFilesException ex) {
                exceptionInitializingVariables = true;
                ex.printStackTrace();
                Logger.getLogger(MapViewerServlet.class.getName()).log(Level.SEVERE, null, ex);

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                Logger.getLogger(MapViewerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            super.init();
        }

	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * @param {HttpServletRequest}  request servlet request
	 * @param {HttpServletResponse} response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nextPage = PagesNames.ERROR_PAGE;//error page, this gets modified if everything is fine.
		mapConfig.updateProperties(configFilePath);
		//its important to erase the basePath becuase it get appended again.
		try {

			response.setContentType("text/html;charset=iso-8859-1");
			HttpSession session = request.getSession();//Se obtiene la sesion

			TreeNode arbolMenuRasters = null;
			//If there was a problem initializing the variables we try to do it again.
			// This code catches information about the exception and displays it for the user.
			try {

				if (exceptionInitializingVariables) {
					initializeVariables();
				}
				arbolMenuRasters = UserRequestManager.createNewRootMenu(request, session);
				exceptionInitializingVariables = false;
			} catch (XMLFilesException ex) {
				//If an exception happens, we start with the default menu
				arbolMenuRasters = UserRequestManager.createNewRootMenu(request, session);
				session.setAttribute("MenuDelUsuario", arbolMenuRasters);
				//If there is an XMLLayerException exception we just display it as
				// warning in the main site
				StringWriter sw = new StringWriter();
				ex.printStackTrace(new PrintWriter(sw));
				String exceptionInfo = ex.getMessage();
				String exceptionTrace = sw.toString();
				exceptionTrace = exceptionTrace.substring(0, exceptionTrace.indexOf("\n"));
				exceptionTrace = exceptionTrace.replace("\"", "");
				exceptionTrace = exceptionTrace.replace("\'", "");
				exceptionInfo = exceptionInfo.replace("\"", "");
				exceptionInfo = exceptionInfo.replace("\'", "");

				request.setAttribute("warningText", exceptionInfo);
				request.setAttribute("warningInfo", exceptionTrace);

				exceptionInitializingVariables = true;
			}

			if (linksVectorialesKmz == null) {
				linksVectorialesKmz = UserRequestManager.getCheckboxKmlLinks(opManager.getVectorLayers());
				HtmlMenuBuilder.vecLinks = linksVectorialesKmz;
			}

			//get menu entry
			MenuEntry[] rasterSelecteLayers = TreeMenuUtils.obtieneMenuSeleccionado(arbolMenuRasters);

			//obtain layer index
			int[] baseLayers = opManager.obtainArrayIndexOfLayers(rasterSelecteLayers);
			Layer curr_main_layer = null;
                        
                        System.out.println("========== CLASS LOCATION DEBUG ==========");

                        System.out.println(
                            LayerMenuManagerSingleton.class
                                .getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                        );
                        
			//this variables are then read by the GlobalJavascript.jsp
			//The next variable is used to decide if the layer has
			// a custom CQL filter
			request.setAttribute("cqlfilter", false);
			if (baseLayers != null) {
				curr_main_layer = opManager.getRasterLayers().get(baseLayers[0]); //get main layer of user
                                
                                System.out.println("========== MAPVIEWER MAINLAYERS ==========");

                                System.out.println(
                                    "mainLayers size = "
                                    + opManager.getRasterLayers().size()
                                );

                                for (int i = 0; i < opManager.getRasterLayers().size(); i++) {

                                    Layer l = opManager.getRasterLayers().get(i);

                                    System.out.println(
                                        "mainLayers[" + i + "]"
                                        + " name=" + l.getName()
                                        + " identity=" + System.identityHashCode(l)
                                        + " multipleDates=" + l.isMultipleDates()
                                    );
                                }

                                System.out.println("========== CURR MAIN LAYER ==========");

                                System.out.println(
                                    "baseLayers[0] = " + baseLayers[0]
                                );

                                System.out.println(
                                    "curr_main_layer = " + curr_main_layer
                                );

                                System.out.println(
                                    "curr_main_layer.name = "
                                    + curr_main_layer.getName()
                                );

                                System.out.println(
                                    "curr_main_layer.identity = "
                                    + System.identityHashCode(curr_main_layer)
                                );

                                System.out.println(
                                    "curr_main_layer.multipleDates = "
                                    + curr_main_layer.isMultipleDates()
                                );

                                System.out.println("======================================");
                                
                                System.out.println("========== MAPVIEWER INSTANCE FINAL ==========");

                                System.out.println(
                                    "Layer: " + curr_main_layer.getName()
                                );

                                System.out.println(
                                    "identityHashCode: "
                                    + System.identityHashCode(curr_main_layer)
                                );

                                System.out.println(
                                    "multipleDates: "
                                    + curr_main_layer.isMultipleDates()
                                );

                                System.out.println("==============================================");
                                
                                System.out.println("========== MAPVIEWER INMEDIATO ==========");

                                System.out.println(
                                    "Layer: " + curr_main_layer.getName()
                                );

                                System.out.println(
                                    "identityHashCode: "
                                    + System.identityHashCode(curr_main_layer)
                                );

                                System.out.println(
                                    "multipleDates INMEDIATO = "
                                    + curr_main_layer.isMultipleDates()
                                );

                                System.out.println("==========================================");
                                
                                System.out.println("========== MAPVIEWER LAYER INSTANCE ==========");

                                System.out.println(
                                    "curr_main_layer = " + curr_main_layer
                                );

                                System.out.println(
                                    "identityHashCode = " + System.identityHashCode(curr_main_layer)
                                );

                                System.out.println(
                                    "name = " + curr_main_layer.getName()
                                );

                                System.out.println(
                                    "multipleDates = " + curr_main_layer.isMultipleDates()
                                );

                                System.out.println(
                                    "layerDetails = " + curr_main_layer.getLayerDetails()
                                );

                                System.out.println("==============================================");
                                
				request.setAttribute("ncwms", curr_main_layer.isncWMS());
				request.setAttribute("currents", curr_main_layer.isoverlayStreamlines());
				request.setAttribute("layerDetails", curr_main_layer.getLayerDetails());
				request.setAttribute("zaxis", curr_main_layer.isZaxis());
                                
                                
				System.out.println("========== MAPVIEWER MULTIPLE DATES ==========");
                                System.out.println("curr_main_layer = " + curr_main_layer);
                                System.out.println("curr_main_layer.name = " + curr_main_layer.getName());
                                System.out.println("curr_main_layer.isMultipleDates() = "
                                        + curr_main_layer.isMultipleDates());
                                
                                System.out.println("========== MAPVIEWER TIME TEST ==========");

                                System.out.println(
                                    "curr_main_layer = "
                                    + curr_main_layer
                                );

                                System.out.println(
                                    "curr_main_layer.name = "
                                    + curr_main_layer.getName()
                                );

                                System.out.println(
                                    "curr_main_layer.isMultipleDates() = "
                                    + curr_main_layer.isMultipleDates()
                                );

                                System.out.println(
                                    "curr_main_layer.getLayerDetails() = "
                                    + curr_main_layer.getLayerDetails()
                                );

                                System.out.println("=========================================");

                                request.setAttribute(
                                    "multipleDates",
                                    curr_main_layer.isMultipleDates()
                                );

                                System.out.println("===============================================");
                                
                                
				request.setAttribute("mainLayer", curr_main_layer.getName());
				request.setAttribute("style", curr_main_layer.getStyle());
				request.setAttribute("max_time_range", curr_main_layer.getMaxTimeLayer());
				request.setAttribute("cqlcols", curr_main_layer.getCql_cols());
				if (!curr_main_layer.getCql_cols().equals("")) {
					request.setAttribute("cqlfilter", true);
				}
			} else {
				request.setAttribute("ncwms", false);
				request.setAttribute("currents", null);
				request.setAttribute("layerDetails", "{}");
				request.setAttribute("zaxis", null);
				request.setAttribute("multipleDates", "null");
				request.setAttribute("mainLayer", null);
				request.setAttribute("style", null);
				request.setAttribute("max_time_range", null);
				request.setAttribute("cqlcols", null);
			}

			//Obtains and set a lenguage.

			String defaultLang = mapConfig.getProperty("defaultLanguage");
			String availableLanguages = mapConfig.getProperty("availableLanguages");
			int imageResolution = Integer.parseInt(mapConfig.getProperty("imageResolution"));

			request.setAttribute("imageResolution", imageResolution);

			request.setAttribute("defaultLanguage", defaultLang);
			request.setAttribute("availableLanguages", availableLanguages);

			//Setting the locale gotten from the one seleted by the user on the website
			String language = request.getParameter("_locale");

			//setting Default locale from the properties file on first time page load
			if (language == null || "".equals(language)) {
				language = defaultLang;
			}

			//get and set background layers

			String defaultBackgroundLayer = mapConfig.getProperty("backgroundLayer");
			String backgroundLayer = request.getParameter("backgroundLayer");
			backgroundLayer = (backgroundLayer == null || "".equals(backgroundLayer)) ? defaultBackgroundLayer : backgroundLayer;
			//get the resolution for a selected background layer
			if (!backgroundLayer.equals(defaultBackgroundLayer)) {
				String[] amr = mapConfig.getProperty("availableMaxResolution").split(";");
				String[] abl = mapConfig.getProperty("availableBackgroundLayers").split(";");
				int i;
				for (i = 0; i < amr.length; i++) {
					if (backgroundLayer.equals(abl[i])) {
						mapConfig.updateProperty("maxResolution", amr[i]);
						break;
					}
				}
			} else {
				mapConfig.updateProperty("maxResolution", mapConfig.getMaxResolution() + "");
			}
			request.setAttribute("backgroundLayer", backgroundLayer);

			//Obtains the selection of the vector layers of the user.

			String[] selectedVectorLayers = UserRequestManager.manageVectorLayersOptions(request, session);
                        System.out.println("========== OPTIONAL LAYER DEBUG ==========");
                        System.out.println("selectedVectorLayers: " + java.util.Arrays.toString(selectedVectorLayers));
                        
			int[] vectorLayers = opManager.obtainIndexForOptionalLayers(selectedVectorLayers);
                        
                        System.out.println("========== OPTIONAL INDEX DEBUG ==========");
                        System.out.println("selectedVectorLayers = "
                                + java.util.Arrays.toString(selectedVectorLayers));
                        System.out.println("vectorLayers = "
                                + java.util.Arrays.toString(vectorLayers));
                        System.out.println("==========================================");
                        
                        System.out.println("vectorLayers: " + java.util.Arrays.toString(vectorLayers));
                        System.out.println("==========================================");

			//openlayers configuration of javascript.
                        
                        System.out.println("========== CREATE CONFIG INPUT ==========");
                        System.out.println("baseLayers = " + java.util.Arrays.toString(baseLayers));
                        System.out.println("vectorLayers = " + java.util.Arrays.toString(vectorLayers));
                        System.out.println("language = " + language);
                        System.out.println("backgroundLayer = " + backgroundLayer);
                        System.out.println("=========================================");
                        
			String openLayerConfig = opManager.createOpenLayConfig(baseLayers, vectorLayers, language, backgroundLayer);

			//This is for the configuration of the page, this are read by the javascript throuhg jsp.

			request.setAttribute("openLayerConfig", openLayerConfig);
			request.setAttribute("language", language);
			//add the link of the vactor layers the one with the checkboxes.
			request.setAttribute("sizeVectLayers", opManager.getVectorLayers().size());
			request.setAttribute("linksKmzVect", linksVectorialesKmz);

			String palette = request.getParameter("paletteSelect");
			if (palette == null && curr_main_layer != null) {
				palette = curr_main_layer.getPalette();
			}
			request.setAttribute("palette", palette);

			//We define the link to request a KML file
			request.setAttribute("linkKML", UserRequestManager.getKmlLink(opManager, baseLayers, palette));
			//Defines the title of the layer
			String layerTitle = UserRequestManager.getTitleOfLayer(opManager, baseLayers, vectorLayers, language);
			//we put the title of the layer next to Gulf of Mexico.
			request.setAttribute("layerTitle", layerTitle);
			request.setAttribute("titleSize", layerTitle.length());
			request.setAttribute("totalLayers", opManager.getTotalVisibleLayers());
			request.setAttribute("_id_first_main_layer", (opManager.getBackgroundLayers()).size());//Index of the  main layer (how many background layers we have)


			request.setAttribute("newSession", session.isNew());//Inidicates if is the first time the map was loaded

			request.setAttribute("paletteUrl", NetCDFRequestManager.getPaletteUrl(curr_main_layer, palette));

			//Contains configuration of the Map like zoom, center, origin, etc.
			request.setAttribute("mapConfig", mapConfig.toJSONObject());

			//Setting the animation URL if any
			String animUrl = request.getParameter("animationURL");
			request.setAttribute("animationURL", animUrl == null ? "" : animUrl);

			//----verificar si llega hasta aqui
			//redirect the page.
			nextPage = PagesNames.MAIN_PAGE;
			//Detect Mobile Browser
			String mobileGet = request.getParameter("mobile");
			String ua = request.getHeader("User-Agent").toLowerCase();
			if (ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*")
					|| ua.indexOf("iphone") != -1 || ua.indexOf("ipad") != -1
					|| ua.indexOf("htc_flyer") != -1 || ua.indexOf("maemo") != -1 || ua.indexOf("tablet") != -1 || ua.indexOf("hpwos") != -1 || ua.indexOf("playbook") != -1
					|| ua.indexOf("palm") != -1 || ua.indexOf("webos") != -1
					|| ua.substring(0, 4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")
					|| (mobileGet != null && mobileGet.equalsIgnoreCase("true"))
					|| (request.getParameter("mobile") != null && request.getParameter("mobile").equals("true"))) {
				nextPage = PagesNames.MOBILE_PAGE;
				request.setAttribute("mobile", "true");//get get variable to true
			} else {
				request.setAttribute("mobile", "false");
			}

		} catch (XMLFilesException ex) {
			request.setAttribute("errorText", "Unable to parse XML files: " + ex.getMessage());
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			String exceptionTrace = sw.toString();
			request.setAttribute("traceText", exceptionTrace);

			Logger.getLogger(MapViewerServlet.class.getName()).log(Level.SEVERE, null, ex);

		} catch (Exception ex) {
			request.setAttribute("errorText", "Inialization Exception: " + ex.getMessage());
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			String exceptionTrace = sw.toString();
			request.setAttribute("traceText", exceptionTrace);

			Logger.getLogger(MapViewerServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
		RequestDispatcher view = request.getRequestDispatcher(nextPage);
		view.forward(request, response);
	}
	
	/**
	 * Processes requests for both HTTP
	 * <code>GET</code> and
	 * <code>POST</code> methods.
	 *
	 * Logger.getLogger(MapViewerServlet.class.getName()).log(Level.SEVERE, null, ex);
	 * } catch (Exception ex) {
	 * request.setAttribute("errorText", "Exception: " + ex.getMessage());
	 * StringWriter sw = new StringWriter();
	 * ex.printStackTrace(new PrintWriter(sw));
	 * String exceptionTrace = sw.toString();
	 * request.setAttribute("traceText", exceptionTrace);
	 *
	 * Logger.getLogger(MapViewerServlet.class.getName()).log(Level.SEVERE, null, ex);
	 * }
	 *
	 *
	 * RequestDispatcher view = request.getRequestDispatcher(nextPage);
	 * view.forward(request, response);
	 *
	 * }
	 *
	 * /**
	 * Handles the HTTP
	 * <code>GET</code> method.
	 *
	 * @param {httpServletRequest} request servlet request
	 * @param {HttpServletResponse} response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
	
	/**
	 * Handles the HTTP
	 * <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
}