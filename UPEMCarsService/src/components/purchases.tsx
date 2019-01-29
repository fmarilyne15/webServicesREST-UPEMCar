import { withStyles, WithStyles } from '@material-ui/core/styles';

import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Typography from '@material-ui/core/Typography';

import * as React from 'react';

const decoratedPurchases= withStyles(theme => ({
    button: {
        backgroundColor: '#EA5A0B',
        color: "#FFF",
        marginRight: '8%',
    },
    fab: {
        backgroundColor: '#000',
    },
    inputBase: {
        width: theme.spacing.unit * 50,
    },
    paper: {
        marginLeft: '10%',
        marginTop: -10,
        width: '80%'
    },
    resetButton: {
        backgroundColor: 'red',
        color: "#FFF",
        marginRight: '20%',
    },
    row: {
        '&:nth-of-type(odd)': {
            backgroundColor: theme.palette.background.default,
        },
    },
    tableCell: {
        color: theme.palette.common.white,
        fontSize: 14,
    },
    tableHead: {
        backgroundColor: '#EA5A0B',
    },
    typography: {
        backgroundColor: '#EA5A0B',
        color: "#FFF",
        fontSize: 20
    }
}));

interface IBasketProps {
    readonly data: any[];
    readonly currencyName: string,
}

export const PurchasesCustomized = decoratedPurchases(class extends React.Component<IBasketProps & WithStyles<'button' | 'inputBase' | 'fab' | 'paper' | 'resetButton' | 'row' | 'tableHead' | 'tableCell' | 'typography'>, {}>{

    constructor(props: any) {
        super(props);
    }

    public render() {
        const { classes, data, } = this.props;
        return (
            <div>
                <Paper className={classes.paper} elevation={8} square={true}>
                    <Typography> Vous avez acheté {data.length} véhicule(s)</Typography>
                    <Table>
                        <TableHead className={classes.tableHead}>
                            <TableRow >
                                <TableCell className={classes.tableCell}>Genre</TableCell>
                                <TableCell className={classes.tableCell}>Immatriculation</TableCell>
                                <TableCell className={classes.tableCell}>Marque</TableCell>
                                <TableCell className={classes.tableCell}>Carburant</TableCell>
                                <TableCell className={classes.tableCell}>An. Circulation</TableCell>
                                <TableCell className={classes.tableCell}>Livraison</TableCell>
                                <TableCell className={classes.tableCell}>Prix</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {data.map((vehicle, index) => {
                                return (
                                    <TableRow className={classes.row} key={index}>
                                        <TableCell component="th" scope="row">{vehicle.kind}</TableCell>
                                        <TableCell >{vehicle.mat}</TableCell>
                                        <TableCell >{vehicle.mark}</TableCell>
                                        <TableCell >{vehicle.fuel}</TableCell>
                                        <TableCell >{vehicle.circulated}</TableCell>
                                        <TableCell >{vehicle.delivery}</TableCell>
                                        <TableCell >{vehicle.price}</TableCell>
                                    </TableRow>
                                );
                            })
                            }
                        </TableBody>
                    </Table>
                </Paper>
            </div>
        )
    }
});
