package io.gatling.karaf

import org.apache.karaf.shell.console.OsgiCommandSupport
import org.apache.karaf.shell.commands.Command
import org.apache.karaf.shell.commands.Option
import io.gatling.core.config.{GatlingConfiguration, GatlingPropertiesBuilder}
import io.gatling.app.Gatling
import io.gatling.core.scenario.Simulation
import scala.collection.mutable

/**
 * Created by btopping on 3/3/14.
 */
@Command(scope = "gatling", name = "gatling", description = "Packaging to run Gatling inside the Karaf container")
class GatlingKarafCommand extends OsgiCommandSupport {
  @Option(name = "-nr", aliases = Array("--no-reports"), description = "Runs simulation but does not generate reports", required = false, multiValued = false)
  private var noReports: Boolean = false
  @Option(name = "-m", aliases = Array("--mute"), description = "Runs in mute mode: don't asks for run description nor simulation ID, use defaults", required = false, multiValued = false)
  private var mute: Boolean = false
  @Option(name = "-ro", aliases = Array("--reports-only"), description = "Generates the reports for the simulation in <directoryName>", required = false, multiValued = false) private
  var reportsOnly: String = null
  @Option(name = "-df", aliases = Array("--data-folder"), description = "Uses <directoryPath> as the absolute path of the directory where feeders are stored", required = false, multiValued = false)
  private var dataDirectory: String = null
  @Option(name = "-rf", aliases = Array("--results-folder"), description = "Uses <directoryPath> as the absolute path of the directory where results are stored", required = false, multiValued = false)
  private var resultsDirectory: String = null
  @Option(name = "-bf", aliases = Array("--request-bodies-folder"), description = "Uses <directoryPath> as the absolute path of the directory where request bodies are stored", required = false, multiValued = false)
  private var requestBodiesDirectory: String = null
  @Option(name = "-sf", aliases = Array("--simulations-folder"), description = "Uses <directoryPath> to discover simulations that could be run", required = false, multiValued = false)
  private var sourcesDirectory: String = null
  @Option(name = "-sbf", aliases = Array("--simulations-binaries-folder"), description = "Uses <directoryPath> to discover already compiled simulations", required = false, multiValued = false)
  private var binariesDirectory: String = null
  @Option(name = "-s", aliases = Array("--simulation"), description = "Runs <className> simulation", required = true, multiValued = false)
  private var simulationClass: String = null
  @Option(name = "-on", aliases = Array("--output-name"), description = "Use <name> for the base name of the output directory", required = false, multiValued = false)
  private var outputDirectoryBaseName: String = null
  @Option(name = "-sd", aliases = Array("--simulation-description"), description = "A short <description> of the run to include in the report", required = false, multiValued = false)
  private var runDescription: String = null

  override def doExecute(): AnyRef = {
    System.out.println("Executing command gatling")

    val builder: GatlingPropertiesBuilder = new GatlingPropertiesBuilder
    if (mute) builder.mute
    if (noReports) builder.noReports
    if (reportsOnly != null) builder.reportsOnly(reportsOnly)
    if (dataDirectory != null) builder.dataDirectory(dataDirectory)
    if (resultsDirectory != null) builder.resultsDirectory(resultsDirectory)
    if (requestBodiesDirectory != null) builder.requestBodiesDirectory(requestBodiesDirectory)
    if (sourcesDirectory != null) builder.sourcesDirectory(sourcesDirectory)
    if (binariesDirectory != null) builder.binariesDirectory(binariesDirectory)
    if (simulationClass != null) builder.simulationClass(simulationClass)
    if (outputDirectoryBaseName != null) builder.outputDirectoryBaseName(outputDirectoryBaseName)
    if (runDescription != null) builder.runDescription(runDescription)

    val clazz: Class[Simulation] = Class.forName(simulationClass).asInstanceOf[Class[Simulation]]
    val build: mutable.Map[String, Any] = builder.build
    GatlingConfiguration.setUp(build)
    new Gatling(Some(clazz)).start

    null
  }
}
